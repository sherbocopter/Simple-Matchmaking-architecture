# Simple-Matchmaking-architecture
A simple matchmaking project for the advanced oop course.

What it does:
--------------
Clients connect to server, enter their credentials and a matchmaking level range. The server places them in a queue until a match is found.
After a match is found, the clients can chat via p2p connection, while still connected to the server for other purposes (ex: multiple matches at once, not yet supported).

Issues:
--------------
- Being an educational project, it may (it does) contain hardcoded stuff, such as the ip address or the ports assigned by the server for the p2p connection.
- The chat is in need of a GUI.
- Disconnects are not managed properly.
- Server should have a separate console thread for various commands.
- Clients have a fixed action flow.

**Note:** It's an educational project built in a weekend, nothing fancy.
