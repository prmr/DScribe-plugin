# The Eclipse Plugin for DScribe

[<img src="DScribeUpdateSite/assets/mcgill-logo.png" alt="McGill Logo" width="150"/>](https://www.mcgill.ca/)&nbsp;&nbsp;&nbsp;&nbsp;
[<img src="DScribeUpdateSite/assets/dscribe-logo.PNG" alt="DScribe Logo" width="450"/>](https://github.com/prmr/DScribe)&nbsp;&nbsp;&nbsp;&nbsp;
[<img src="DScribeUpdateSite/assets/eclipse-logo.png" alt="Eclipse Logo" width="150"/>](https://www.eclipse.org/)


## Overview
* Do you frequently copy-paste exisiting unit tests to test a recurrent specification?
* Do you frequently copy-paste existing documentation fragments to document a recurrent specification?
* Do you find it annoying to update both the corresponding unit tests AND documentation fragments when a specification evolves?

If you answered yes to any of the above questions, then DScribe is the right tool for you!</p>

DScribe allows you to test and document recurring concerns consistently and with ease. You simply define joint templates to capture 
the structure to test and document a specification. Then, you use method-level Java annotations to invoke the templates.

To learn more about DScribe, visit the [DScribe GitHub repository](https://github.com/prmr/DScribe).


## Documentation 
**Creating and Invoking DScribe Templates** <br>
To learn how to create and invoke your own DScribe templates, visit DScribe's [README](https://github.com/prmr/DScribe#how-dscribe-works).

**Running DScribe in Eclipse** <br>
To generate unit tests and documentation for a production class(es):
1. Select the target class(es) in Eclipse's ``Project Explorer`` view.
     * To select one class, simply click on it with your mouse.
     * To select multiple classes, hold down the ``Ctrl key`` and click on each class one by one.
2. Perform a ``Right`` click. The Eclipse dropdown menu will appear.
3. Click the ``Run DScribe`` command in the dropdown menu (see [screenshot](https://github.com/prmr/DScribe-plugin/blob/main/DScribeUpdateSite/assets/dscribe-command.png)).

Once DScribe terminates, a message dialog will appear to display the execution results. If DScribe runs successfully, the generated documentation 
and unit tests will be added to the production class and its corresponding test class, respectively.

**Publications**
* Mathieu Nassif, Alexa Hernandez, Ashvitha Sridharan, and Martin P. Robillard. [Generating Unit Tests for Documentation](https://ieeexplore-ieee-org.proxy3.library.mcgill.ca/document/9447988). To appear in <i>IEEE Transactions on Software Engineering</i>, 12 pages, 2021. 

## Download
DScribe is distributed under the terms of the [Apache License v 2.0](https://github.com/prmr/DScribe-plugin/blob/main/LICENSE.txt). To install the DScribe Eclipse plugin, follow the steps below.

**System requirements:** Eclipse 3.4, JavaSE 14. DScribe is currently tested on Windows only.

**To install DScribe:**
1. From the Eclipse menu bar, select ``Help > Install New Software``.
2. Click the ``Add`` button. This Add Repository window will appear.
3. For the name, enter *DScribe*. For the Location, enter *https://www.cs.mcgill.ca/~martin/DScribeUpdateSite*. Click ``Add``.
5. Select the new ``DScribe`` entry in the list and click ``Next`` twice.
6. Select that you accept the terms of the license agreement and click ``Finish``.
7. Restart your workspace.

## Contact 
For any questions, concerns, or suggestions, please free feel to contact [Alexa Hernandez](mailto:alexa.hernandez@mail.mcgill.ca).
