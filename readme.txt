Distributed dummy (simplified) socket-based command-line calculator,
which demonstrates Client -> Server Apache Mina usage, below are few use-cases:

1. Client sends operations and arguments to server.
2. Server, being provided Operator and Arguments, computes the result and sends it back to Client.
Examples:
- Client → Server (Operator and Arguments):
    Operator is one of: “[PLUS|MINUS|MULTIPLY|DIVIDE]”
    Arguments are always positive numbers, between 0-65535 (including 0 and 65535).
    The operator and the numbers are each exactly one space separated
    Example: “PLUS 4325 983 0”
- Server → Client (Operator and arguments):
    Server performs calculations, and sends back result to client.
    Answer is always prefixed by the string “RESULT”, with following actual result
- Client → Server (Teardown):
    Client sends the string “CLOSE” to Server
- Server → Client (Teardown):
    Server sends the string “CLOSED” to the client and concludes the transaction.
------------------------------------------------------------------------------------------------------------------------

About Apache mina:
Apache MINA is multi-purpose open-source framework for Network Applications.
MINA provides unified APIs for various transports like TCP, UDP etc. It provides both high-level and low-level network APIs.