# Overview

Well, the program is designed to send SMS messages via email. As it turns out, Gmail has domains for each major mobile operator in the U.S. and allows you to send a message directly to a phone by entering the number with the appropriate domain. Initially, services like AWS SNS and Twilio were considered but were dropped due to the number of requirements. Now, the software can send messages to both an individual and a list of recipients, as the main list of numbers and users is stored in a CSV file. The program includes a loop to go through the entire list. There is also the option to manually select the recipients or let the program send messages to contacts whose date is one day ahead of the current date.

The video (a little over 5 minutes) shows what the program looks like and what it does.

[Software Demo Video](https://youtu.be/BuWAS29Nw-8)

# Development Environment

I had to choose a new IDE, and based on recommendations, I went with IntelliJ IDEA. I had to connect Apache Maven to it in order to pull in unique libraries. IntelliJ is available for free to students, but otherwise, it’s paid. Java was chosen because I was interested in getting to know the language, not because it's more convenient for building such applications.

# Useful Websites

- [IntelliJ IDEA docks](https://www.jetbrains.com/help/idea/getting-started.html)
- [ChatGPT](https://chatgpt.com/)
- [How to bring Maven into IntelliJ](https://www.jetbrains.com/help/idea/maven-support.html)
- [Useful things to send SMS with Gmail](https://www.gmass.co/blog/send-text-from-gmail/)
- [Sending emails with JAVA(Gmail)](https://developers.google.com/gmail/api/guides/sending)

# Future Work

- Make it cleaner and more readable
- Automatically import CSV from Google Sheets
- Make it an EXE
