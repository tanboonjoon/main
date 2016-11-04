Testcase ID: TC001 <br>
Title : Loading Sample test data <br>
Description : Tester should be able to load the sample data successfully <br>
Assumption : Tester has yet to open the app once <br>

Steps no. | Steps | ExpectedResult
--- | :---------------- | :----------------
1 | create a folder call 'taskForceTest' on desktop | a folder called 'taskForceTest' should be created
2  | download the jar file and place it inside 'taskForceTest' folder | the folder should contain the downloaded jar file
3 | open the jar file | TaskForce window should appear. config, perferences and log file should be generated in the same folder
4 | close the app | TaskForce app should be closed
5 | Transfer the forDemoUse.xml in  'ManualTesting' folder and overwrite the forDemoUse file in 'taskForceTest' | the forDemoUse.xml in 'taskForceTest' should be overwritten by the one in 'ManualTeting folder'
6 | open the app again | TaskForce should open with some tasks shown on the list
