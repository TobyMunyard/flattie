# Flattie

### What is Flattie?
Flattie is a web application designed to help students manage their flatting experience, and it's also useful for property managers. It is being developed by students from the INFO310 Software Project Management course at the University of Otago.

### Accessing Flattie

To run Flattie on your computer, you'll need to follow a few simple steps:

1. **Install Java:**
   You need to install Java Development Kit (JDK) version 17 or newer. If you don't have it yet, you can download it from [here](https://adoptopenjdk.net/) or from any other trusted source.

2. **Get the Project:**
   - Click the green "Code" button at the top of this page.
   - Choose your preferred method to download the project. You can either clone the repository using Git or download it as a ZIP file (if you're unfamiliar with Git, downloading the ZIP file might be easier).

3. **Set Up Your Computer:**
   - Open a terminal (Command Prompt on Windows or Terminal on macOS/Linux).
   - Navigate to the directory where you saved Flattie. You can do this by typing `cd <path-to-flattie>` in the terminal. For example:
     ```
     cd C:\\Users\\YourName\\Downloads\\flattie
     ```

4. **Build the Project:**
   - On Windows, type: 
     ```
     gradlew build
     ```
   - On macOS/Linux, type:
     ```
     ./gradlew build
     ```

5. **Run the Application:**
   - On Windows, type:
     ```
     gradlew bootRun
     ```
   - On macOS/Linux, type:
     ```
     ./gradlew bootRun
     ```

6. **Open Flattie in Your Browser:**
   - Once everything is running, you can open Flattie by going to your web browser and typing:
     ```
     localhost:8080
     ```
     This will bring up the home page of the application.
   - If you want to view the database directly, you can also go to:
     ```
     localhost:8080/h2-console
     ```
     Log in with the username **"sa"** and leave the password blank.

### Using Flattie Features

To use most of Flattie's features, you will need to create an account. Here’s how to get started:

1. **Create an Account:**
   - On the top navigation bar, you'll find the "Create Account" and "Login" options. Click "Create Account" to sign up.
   - Alternatively, you can use the pre-made "Tester" account with the username **"Tester"** and password **"test1234"** to explore.

2. **Explore New Features:**
   - After signing up, you’ll see additional options in the navigation bar, such as creating and joining flats.

3. **Join a Flat:**
   - To access more features (like contacting property managers), you need to join a flat. 
   - You can join the test flat using the code **"1234"**, or you can create your own flat and automatically be added to it.

### Technologies Behind Flattie

Flattie is built with the following technologies:
- **Backend:** [Java](https://www.java.com/en/) with the [Spring](https://spring.io/) framework
- **Frontend:** Standard HTML, CSS, and JavaScript, using the [Thymeleaf](https://www.thymeleaf.org/) template engine to connect the frontend and backend.
- **Database:** The data is stored temporarily in an [H2](https://www.h2database.com/html/main.html) SQL database, which is useful for testing and ease of use.

### Project Documentation
- [Project Management Sheet](https://otagouni-my.sharepoint.com/:x:/r/personal/claca067_student_otago_ac_nz/Documents/INFO310_Project_Management_Template_v1.xlsx?d=w757b37d9e9464d6b9e71a45096e7c35c&csf=1&web=1&e=q7btoE)
- [Test Cases Sheet](https://otagouni-my.sharepoint.com/:x:/g/personal/munto996_student_otago_ac_nz/EeXHTrGbHcZKqObsVfxbZfcB9Xcl8IfszyXp6Y8CJZIMUw?e=iLY5hz)
- [Milestone One Slides](https://otagouni-my.sharepoint.com/:p:/g/personal/munto996_student_otago_ac_nz/ERJHrdyYuyNEk_DKz6XG5OIBI0At0_OlHR-CX5dC2NnjaA?e=OSpul3)
- [Risk Sheet](https://otagouni-my.sharepoint.com/:x:/g/personal/munto996_student_otago_ac_nz/ESK3Y5bN101PlSYBQOzHdxsB81JnFpmG4ZtUYkwM-R4Z_w?e=xuyFVs)
- [Final Presentation Slides](https://otagouni-my.sharepoint.com/:p:/g/personal/munto996_student_otago_ac_nz/ESSlnhQS7AVPiPVioYoJTYoBfPMMSHi2gXc6fefwzAaOdg?e=OnmouN)