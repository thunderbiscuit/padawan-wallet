# Contributing to Padawan Wallet

First off, thanks for taking the time to contribute! 🎉🎉

When contributing to this repository, please first discuss the change you wish to make via issue with the maintainers of this repository before making a change. These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.

## Code of Conduct

This project and everyone participating in it is governed by the [Padawan Wallet Code of Conduct](), please follow it in all your interaction with the project. By participating, you are expected to uphold this code. Please report unacceptable behavior to @thunderbiscuit


## Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a
   build. Add only relevant files to commit and ignore the rest to keep the repo clean.
2. Update the README.md with details of changes to the interface, this includes new environment
   variables, exposed ports, useful file locations and container parameters.
3. You should request review from the maintainers once you submit the Pull Request.

## Instructions

- Git Workflow

```bash
## Step 1: Fork Repository

## Step 2: Git Set Up & Download
# Clone the repo
$ git clone https://github.com/<User-Name>/<Repo-Name>
# Add upstream remote
$ git remote add upstream https://github.com/thunderbiscuit/padawan-wallet
# Fetch and merge with upstream/master
$ git fetch upstream
$ git merge upstream/master

## Step 2: Create and Publish Working Branch
$ git checkout -b <type>/<issue|issue-number>/{<additional-fixes>}
$ git push origin <type>/<issue|issue-number>/{<additional-fixes>}

## Types:
# wip - Work in Progress; long term work; mainstream changes;
# feat - New Feature; future planned; non-mainstream changes;
# bug - Bug Fixes
# exp - Experimental; random experimental features;
```

- On Task Completion:

```bash
## Committing and pushing your work
# Ensure branch
$ git branch
# Fetch and merge with upstream/master
$ git fetch upstream
$ git merge upstream/master
# Add untracked files
$ git add .
# Commit all changes with appropriate commit message and description
$ git commit -m "your-commit-message" -m "your-commit-description"
# Fetch and merge with upstream/master again
$ git fetch upstream
$ git merge upstream/master
# Push changes to your forked repository
$ git push origin <type>/<issue|issue-number>/{<additional-fixes>}

## Creating the PR using GitHub Website
# Create Pull Request from <type>/<issue|issue-number>/{<additional-fixes>} branch in your forked repository to the master branch in the upstream repository
# After creating PR, add a Reviewer (Any Admin) and yourself as the assignee
# Link Pull Request to appropriate Issue, or Project+Milestone (if no issue created)
# IMPORTANT: Do Not Merge the PR unless specifically asked to by an admin.
```

- After PR Merge

```bash
# Delete branch from forked repo
$ git branch -d <type>/<issue|issue-number>/{<additional-fixes>}
$ git push --delete origin <type>/<issue|issue-number>/{<additional-fixes>}
# Fetch and merge with upstream/master
$ git checkout master
$ git pull upstream
$ git push origin
```

- Always follow [commit message standards](https://chris.beams.io/posts/git-commit/)
- About the [fork-and-branch workflow](https://blog.scottlowe.org/2015/01/27/using-fork-branch-git-workflow/)
