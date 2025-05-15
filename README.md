# Flattie
## What is Flattie?
Flattie is a flat management web application intended to be used by students who are flatting as well as property managers. It is being developed by students of [INFO310 Software Project Management](https://www.otago.ac.nz/courses/papers?papercode=INFO310) at [The University of Otago](https://www.otago.ac.nz/).

## Accessing Flattie
1. Install a [Java JDK](https://www.oracle.com/middleeast/java/technologies/downloads/) of your choosing, version 17 or newer is required.
2. Clone this repository by clicking the green "code" button at the top of the page and cloning in whatever way you wish.
3. Open a terminal of your choosing.
4. Run "cd \<whatever directory you saved flattie to>"
5. Once in the correct directory, run "gradlew build" on windows or "./gradlew build" on linux/mac.
6. Run "gradlew run" on windows or "./gradlew run" on linux/mac.
7. If you are not already there, move to the bottom of the command output and click the locahost:8080 link or visit localhost:8080 in your web browser to open the home page.

## Using Flattie Features
* Most features are locked behind having a account. This can be done by visiting the "create account" and "login" pages from the top navigation bar.
* After an account has been created you will see several new items appear in the navigation bar. These include creating flats and joining flats.
* In order to access further features such as contacting property managers you will need to join a flat. This can be done by joining the test flat using code "1234" or you could create your own, which you will be automatically added to.

## Languages
Flattie's backend is built using [Java](https://www.java.com/en/) with [Spring](https://spring.io/). The frontend is built in standard HTML, CSS and JavaScript with the [Thymeleaf](https://www.thymeleaf.org/) template engine to merge the content of the front and back. The data is currently stored in a in-memory [H2](https://www.h2database.com/html/main.html) SQL database for the purposes of testing and ease of use.

## Project Documentation
[Project Management Sheet](https://otagouni-my.sharepoint.com/:x:/r/personal/claca067_student_otago_ac_nz/Documents/INFO310_Project_Management_Template_v1.xlsx?d=w757b37d9e9464d6b9e71a45096e7c35c&csf=1&web=1&e=q7btoE)

[Test Cases Sheet](https://otagouni-my.sharepoint.com/:x:/g/personal/munto996_student_otago_ac_nz/EeXHTrGbHcZKqObsVfxbZfcB9Xcl8IfszyXp6Y8CJZIMUw?e=iLY5hz)

[Milestone One Slides](https://otagouni-my.sharepoint.com/:p:/g/personal/munto996_student_otago_ac_nz/ERJHrdyYuyNEk_DKz6XG5OIBI0At0_OlHR-CX5dC2NnjaA?e=OSpul3)

[Risk Sheet](https://otagouni-my.sharepoint.com/:x:/g/personal/munto996_student_otago_ac_nz/ESK3Y5bN101PlSYBQOzHdxsB81JnFpmG4ZtUYkwM-R4Z_w?e=xuyFVs)

[Final Presentation Slides](https://otagouni-my.sharepoint.com/:p:/g/personal/munto996_student_otago_ac_nz/ESSlnhQS7AVPiPVioYoJTYoBfPMMSHi2gXc6fefwzAaOdg?e=OnmouN)
