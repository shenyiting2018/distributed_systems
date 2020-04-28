Yiting Shen
CSE317 - PA-1 Multithreaded Server
2020-04-27

This assignment is to create a web server that could respond to http request and send static content including HTML, text, images, etc. It should be able to handle different scenarios regarding invalid request (400), not found (404) and permission issue (403).

I wrote this Java-based server to resolve the above challenges. This server keeps running there and uses a socket to listen to a user-specified port. Once a connection is found, it spawn a new thread to handle the request. After some parameter parsing and file retrieving, it send back the desired file if found. Otherwise it throw a 404 not found error. Once the response is sent back, the socket is closed.

Submitted filed:
1. src/  the java directory, this directory contains all the .java files that need to be compiled and run.
2. Makefile, a simple bash script that is used to compile the java files. This should be run before starting the server.
3. server, a Python script that takes the user-input argument and pass the arguments to the java startup command.
4. script_file.txt: another file for requirement 2. 
5. README.txt: this file
5. screenshots: all the screenshots on browser or postman to prove the workable
server. 


Prerequisites:
1. Python
2. Java 8+
3. Bash

Steps:
1. Installed the prerequisites if not installed on the linux/macOS machine
2. Move around the `webserver_files` directory to desired place or leave it there
3. Type in `pwd` in the terminal when entering the `webserver_files` and copy the current absolute path of the directory. This directory should contain a `index.html` file, a `secrets.txt` file and a subdirectory: Home - Santa Clara University_files (downloaded from SCU's website)
4. cd to the directory where you can see `server` and `Makefile` and `src` folder.
5. Build the java binary by simply run: ./Makefile
    * You may need to add access to it by running  `chmod +x Makefile` or run `bash ./Makefile`
    * This step should create a classes folder under `./MyServer/src/main/java`.
6. Now take the path copied in step 3 and run the command:
    `./server -document_root <file_path> -port <port_number>
7. You should see the printout:
   Server started at port: <your port>



