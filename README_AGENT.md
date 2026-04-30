# Autonomous Android CI/CD Pipeline — How It Works

This repo runs a **fully autonomous coding pipeline**. You raise a GitHub Issue; the code gets written, reviewed, fixed if needed, and merged to `main` — with zero human involvement.

---

## The Full Flow

```
You raise an Issue
        |
        v
[claude-agent.yml]
  Claude reads the issue, explores the codebase,
  implements the feature/fix, runs ./gradlew to verify,
  opens a PR, and enables auto-merge.
        |
        v
Parallel checks on the PR:
  ┌─────────────────────────┐   ┌──────────────────────────┐
  │   [android-ci.yml]      │   │   [claude-review.yml]    │
  │   Build Debug APK       │   │   Claude reads CLAUDE.md │
  │   Unit Tests            │   │   + the PR diff and      │
  │   Lint Check            │   │   checks every rule.     │
  └────────────┬────────────┘   └────────────┬─────────────┘
               |                             |
               |                    PASS     |     FAIL
               |               ┌────────────┴──────────────┐
               |               |                           |
               |       label: review: passed      label: review: blocked
               |               |                           |
               |               |               [claude-autofix.yml]
               |               |                 Claude reads the inline
               |               |                 review comments, fixes
               |               |                 every violation, pushes
               |               |                 to the PR branch.
               |               |                 (max 3 attempts)
               |               |                           |
               |               |               Push triggers re-review
               |               |               loop until PASS or 3 fails
               |               |                           |
               └───────────────┴───────────────────────────┘
                                       |
                              All 4 checks green
                                       |
                                       v
                               Auto-merge to main
                                       |
                                       v
                           [rollback.yml] — runs CI
                           on main after every merge.
                           If it fails, reverts the
                           commit automatically.
```

---

## The 5 Workflows

### 1. `claude-agent.yml` — The Implementer
**Trigger:** Issue labeled `claude: fix` or `claude: implement`

Claude checks out the repo, reads `CLAUDE.md` for project rules, explores the codebase, writes the code, runs `./gradlew assembleDebug` and `./gradlew test` to verify, then:
- Creates a branch: `claude/fix-issue-N-description` or `claude/feat-issue-N-description`
- Commits: `fix: description (#N)` or `feat: description (#N)`
- Opens a PR targeting `main`
- Enables auto-merge: `gh pr merge --auto --squash --delete-branch`

Auto-merge queues the PR to land on `main` the moment all required checks pass — no human approval needed.

---

### 2. `android-ci.yml` — The Build Gate
**Trigger:** Every PR opened or updated

Runs three checks in parallel:

| Check | What it does |
|---|---|
| `Build Debug APK` | `./gradlew assembleDebug` — must compile |
| `Unit Tests` | `./gradlew test` — all tests must pass |
| `Lint Check` | `./gradlew lint` — no lint errors allowed |

All three are **required status checks** on `main`. Auto-merge will not fire until all three are green.

---

### 3. `claude-review.yml` — The Reviewer
**Trigger:** Every PR opened or updated

Claude reads `CLAUDE.md` (the source of truth for project rules) and diffs the PR against every mandatory rule:

- **Gradient design system** — every background and button must use `Brush.linearGradient` / `verticalGradient`. Flat solid colours are forbidden.
- **Text on gradients** — must be `Color.White` or `Color.White.copy(alpha = 0.87f)`.
- **MVVM architecture** — ViewModels must not import `android.view.*` or hold `Context`. No `!!` operators.
- **Tests** — any new ViewModel or Repository needs unit tests for happy path, error, and loading states.
- **Accessibility** — every `Image` and `Icon` must have a non-empty `contentDescription`.
- **No hardcoded strings** — all user-facing text must live in `strings.xml`.

Claude posts inline comments for line-specific violations and a top-level summary comment. Then it applies a verdict label:

| Verdict | Label applied | Effect |
|---|---|---|
| No violations | `review: passed` | Check goes green, auto-merge proceeds |
| Any violation | `review: blocked` | Check fails, auto-merge blocked, autofix fires |

`Claude Code Review` is a **required status check** — the PR cannot merge without it passing.

---

### 4. `claude-autofix.yml` — The Self-Healer
**Trigger:** `review: blocked` label added to a PR

This workflow uses `pull_request_target` so it **always reads from `main`** — not the PR branch. This means any fix pushed to `main` takes effect on all open PRs immediately.

**What it does:**
1. Counts prior attempts via `autofix: attempt-N` labels (max 3).
2. Checks out the PR branch with write access.
3. Runs a Claude agent that reads the review's inline comments and summary, fixes every blocking violation, runs `./gradlew assembleDebug` and `./gradlew test`, commits, and pushes.
4. Removes `review: blocked` from the PR.
5. The push triggers `claude-review.yml` to re-run automatically.
6. If the fix is clean, the review applies `review: passed` and auto-merge fires.

**Loop guard:** After 3 failed attempts, Claude posts a "manual intervention required" comment and stops. The `autofix: attempt-N` labels make the attempt count visible at a glance in the PR.

---

### 5. `rollback.yml` — The Safety Net + APK Delivery
**Trigger:** Every push to `main`

Two jobs run sequentially:

**Job 1 — `guard`:** Runs `assembleDebug` + `test` on `main`. If any check fails, it automatically reverts the breaking commit and opens an incident issue:

```bash
git revert HEAD --no-edit
git push origin main
```

**Job 2 — `deliver`:** Runs only when `guard` passes (never fires on a build that gets auto-reverted). It:
1. Downloads the APK that `guard` already built (no duplicate compile).
2. Creates a GitHub Release tagged `build-<sha>` with the APK attached.
3. POSTs a formatted Slack notification with a one-click **Download APK** button.

This means `main` is always in a working state, and your team gets the fresh APK in Slack within minutes of every successful merge — zero manual steps.

---

## Required Secrets

| Secret | What it is | How to get it |
|---|---|---|
| `CLAUDE_CODE_OAUTH_TOKEN` | Authenticates the Claude Code agent | Run `claude setup-token` in your terminal |
| `AGENT_PAT` | A GitHub Personal Access Token | GitHub → Settings → Developer settings → Personal access tokens → Fine-grained: `contents`, `pull-requests`, `issues`, `workflows` write |
| `SLACK_WEBHOOK_URL` | Slack incoming webhook for APK delivery notifications | Slack workspace → Apps → Incoming Webhooks → add to channel → copy URL |

`AGENT_PAT` is needed because PRs and labels created by `GITHUB_TOKEN` (the default) do not trigger downstream workflows — GitHub blocks this to prevent infinite loops. A PAT belonging to a real user bypasses this restriction.

---

## Required Labels

Create these once with `gh label create`:

```bash
gh label create "claude: fix"          --color "EF4444" --description "Agent: fix a bug"
gh label create "claude: implement"    --color "6366F1" --description "Agent: implement a feature"
gh label create "claude: implemented"  --color "10B981" --description "Agent finished"
gh label create "review: passed"       --color "0E8A16" --description "Claude review approved"
gh label create "review: blocked"      --color "D93F0B" --description "Claude review found blocking violations"
gh label create "autofix: attempt-1"   --color "FBCA04" --description "Claude autofix attempt 1 of 3"
gh label create "autofix: attempt-2"   --color "F9A825" --description "Claude autofix attempt 2 of 3"
gh label create "autofix: attempt-3"   --color "E65100" --description "Claude autofix attempt 3 of 3"
```

---

## Branch Protection on `main`

Required status checks (all must pass before merge):

```
Build Debug APK
Unit Tests
Lint Check
Claude Code Review
```

No human review required. Auto-merge handles the rest once all 4 checks are green.

---

## CLAUDE.md — The Rules File

`CLAUDE.md` at the repo root is the single source of truth for what "correct" code looks like in this project. Both the implementer agent (`claude-agent.yml`) and the reviewer (`claude-review.yml`) read it before doing any work.

If you want to change a rule — add a new required pattern, ban a certain API, require a new test type — edit `CLAUDE.md`. The change takes effect on the very next PR.

---

## How to Use

| What you want | How |
|---|---|
| Fix a bug | Issues → New Issue → Bug Report template → submit |
| Add a feature | Issues → New Issue → Feature Request template → submit |
| Trigger manually | Add label `claude: fix` or `claude: implement` to any existing issue |
| Retry a failed agent run | Remove the label, wait 5 seconds, re-add it |
| Override a blocked review | Fix the violation manually, push to the PR branch |

---

## Troubleshooting

| Problem | Cause | Fix |
|---|---|---|
| Agent run is skipped | Label applied is not `claude: fix` or `claude: implement` | Check the exact label name (no typos, case-sensitive) |
| CI not running on PR | PR opened with `GITHUB_TOKEN` | Make sure `AGENT_PAT` secret is set and used in `claude-agent.yml` |
| Auto-merge not firing | "Allow auto-merge" disabled | Repo Settings → General → Pull Requests → enable Allow auto-merge |
| Review never passes | CLAUDE.md rule too strict / agent keeps violating same rule | Read the inline comments; update CLAUDE.md if the rule is impractical |
| Autofix loops 3 times and stops | Claude cannot fix the violation autonomously | Read the "gave up" comment, fix manually, push to the PR branch |
| `gradlew` permission denied | File not executable | `git update-index --chmod=+x gradlew && git push` |
| OAuth token invalid | Token expired or revoked | Re-run `claude setup-token`, update `CLAUDE_CODE_OAUTH_TOKEN` secret |
