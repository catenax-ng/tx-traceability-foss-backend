# Contributing to Eclipse Tractus-X

Thank you for your interest in this project.

## Project description

The companies involved want to increase the automotive industry's
competitiveness, improve efficiency through industry-specific cooperation and
accelerate company processes through standardization and access to information
and data. A special focus is also on SMEs, whose active participation is of
central importance for the network's success. That is why Catena-X has been
conceived from the outset as an open network with solutions ready for SMEs,
where these companies will be able to participate quickly and with little IT
infrastructure investment. Tractus-X is meant to be the PoC project of the
Catena-X alliance focusing on parts traceability.

* https://projects.eclipse.org/projects/automotive.tractusx
* https://github.com/eclipse-tractusx/traceability-foss-backend

## Developer resources

Information regarding source code management, builds, coding standards, and
more.

* https://projects.eclipse.org/projects/automotive.tractusx/developer

The project maintains the source code repositories in the following GitHub organization:

* https://github.com/eclipse-tractusx/

## Eclipse Development Process

This Eclipse Foundation open project is governed by the Eclipse Foundation
Development Process and operates under the terms of the Eclipse IP Policy.

* https://eclipse.org/projects/dev_process
* https://www.eclipse.org/org/documents/Eclipse_IP_Policy.pdf

## Eclipse Contributor Agreement

In order to be able to contribute to Eclipse Foundation projects you must
electronically sign the Eclipse Contributor Agreement (ECA).

* http://www.eclipse.org/legal/ECA.php

The ECA provides the Eclipse Foundation with a permanent record that you agree
that each of your contributions will comply with the commitments documented in
the Developer Certificate of Origin (DCO). Having an ECA on file associated with
the email address matching the "Author" field of your contribution's Git commits
fulfills the DCO's requirement that you sign-off on your contributions.

For more information, please see the Eclipse Committer Handbook:
https://www.eclipse.org/projects/handbook/#resources-commit

### Commit messages
The commit messages have to match a pattern in the form of:
`<type>(optional scope): <Ticket_ID> <description>`

Allowed types are `chore`, `fix` and `feature`.

Examples:
`feature(users): TRACEFOSS-XXX description`
`fix: TRACEFOSS-123 make X work again`

The detailed pattern can be found here: [commit-msg](./dev/commit-msg)

#### How to use
```shell
cp dev/commit-msg .git/hooks/commit-msg && chmod 500 .git/hooks/commit-msg
```

# Static Code Analysis with SonarCloud

## Overview
This project utilizes SonarCloud for static code analysis, which provides feedback on issues such as code smells, bugs, and security vulnerabilities.

## How to Use (IntelliJ Example)
To connect your remote SonarCloud instance with your IntelliJ IDE, follow these steps:

1. Install the SonarLint plugin for IntelliJ:
	* Open IntelliJ and go to File > Settings (or IntelliJ IDEA > Preferences on macOS)
	* Select Plugins and search for "SonarLint"
	* Click on the "Install" button and follow the prompts to complete the installation

2. Configure SonarLint with your SonarCloud account:
	* Go to File > Settings (or IntelliJ IDEA > Preferences on macOS)
	* Select Other Settings > SonarLint
	* Click on the "+ Add" button and select "SonarCloud"
	* Add your project key
	* Click on "Create Token" and log in
	* Copy and paste the token
	* Click on the "Test Connection" button to verify the connection

3. Analyze your code with SonarLint:
	* Open your project in IntelliJ IDEA
	* Right-click on your project folder in the Project Explorer and select "SonarLint > Analyze <project_name>"

By following these steps, you can connect your remote SonarCloud instance with your IntelliJ IDE and analyze your code with SonarLint.

## Contact

Contact the project developers via the project's "dev" list.

* https://accounts.eclipse.org/mailing-list/tractusx-dev
