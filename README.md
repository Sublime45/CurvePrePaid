# CurvePrePaid
This repository holds the source code for the assignment for a prepaid card. The project is a first draft and can have a lot of upgrades.

It was written using Java and MySQL as the underlying database.
A Data Access Manager has been created to handle the connection to the underlying database.

The program offers the following functions:
1. Additional cards per user.
2. Listing of user cards.
3. Loading Funds
4. Show total balance of the user.
5. Show total blocked funds of the user.
6. Transaction recording and listing
7. Blocking of funds by merchant
8. Capturing funds by merchant
9. Releasing funds by merchant.
10. Refunding.

The repo also includes the schema for the database.

This project can be improved a lot and new features added to it. Due to time constraints these features were not implemented.
Nevertheless the undetlying framework and the code architecture has been coded keeping in mind the future improvements and features that can be added 

Future improvements:
1. Deployed as a web service
2. User creation and log in
3. User authorization for the API calls
4. Better exception handling
5. Card numbers are being generated as an inremented integer. In the future card numbers should be created so they can be verified by a check digit and adhere to international banking standards.
6. Multithreading and concurrency can be and should be greatly improved.

