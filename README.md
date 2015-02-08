PSU CS300 Software Engineering Course Project( ChocAn )
 
Main Program:               Kai_Jiang_CS300_ChocAn.jar  (need JVM)

Database:                   EITHER ChocAn.db (can revise) OR ./database/ChocAn.db (original)
                            If something wrong with database, can replace file with ./databse/ChocAn.db

Reports:                    Name@yyyy-MM-dd-HH-mm-ss.txt


DBMS:                       sqlite3*    is a command-line shell for accessing and modifying SQLite databases (denpents on specific OS)
                            it can also view data in database file


Login with ID, Password, Role     (Name (not needed just list) )
           1   1234      Member   Eric
           2   2345      Member   Kai
           7   Google    Manager  Google
           11  PSU       Provider PSU



~~~~ Brief Introductions to Functions of ChocAn System  ~~~~

== Member == 
Member can choose Provider.

== Provider ==
Provider can choose Member Service and add fee to each member's account.

== Manager ==
Manager can choose members to build their reports. (TXT format)
Manager can add different person(include member, manager, provider).
