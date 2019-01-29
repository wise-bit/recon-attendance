# Reconattendance

Reconattendance - A combination of reconnaissance and attendance

## Description

Taking attendance at the beginning of a class is time-consuming, and can take quite long depending on the total population of the class. With the advent of facial recognition, it is a practical idea to use this to take attendance automatically when the students walk into the room. This program does so without using any pre-existing libraries, but using an entirely new algorithm.

## Features

In a nutshell, reconattendance's components include:
1. A program which is able to recognize when a face is in range of the camera.
2. Allocate identification, or interest points on a recognized face in order to identify it.
3. Compare the digital data with data in the database in order to check if they are in the database.
4. Add the person’s attendance status to the database automatically.

(i) The program has an extremely unique method of recognition of facial recognition by primary treatment of images
to enhance certain features. Therefore, this approach not only makes the existing features work better, but also allows
future additions of more features of a face without too much effort. This makes the program very versatile.

(ii) All major features protected under a self-defined encryption system with a secure password to ensure students
cannot change any major aspects of the program without teachers' supervision

(iii) Adding of further versions of a face over time to improve results.

(iv) A generally fully functional attendance app with or without the camera support, to a certain extent

(v) A new and innovative way of processing images and using them for training models and tweaking parameters


### Process Of Training:

![Image of flow of process Of training](/res/process-of-training.jpg)

## Major Skills

(i) Object oriented programming - Teachers, Students, Classes, Faces

(2) Image Processing - The whole project is based on image manipulation

(3) Object comparisons

(4) Mathematical innovation

(5) Significant planning skills for development of new algorithms

## Area of concern:

* The program uses a Webcam library, which was impossible to be coded natively in Java, hence it is based on C code to access hardware. Although tested thoroughly, the webcam interface may fail in specific situations such as unsupported webcams, which is still fixable through minor tweakings.

* The algorithm used in this program is built from the ground up, from scratch. Therefore, it has very minimal training concerning machine learning, and the mathematical capabilities of the models are incomparable to other projects such as OpenCV. Therefore, the results may often be false.

* Due to lack of time, the models were not trained well enough, but if given the chance, this program is capabilities of performing much better over time.

* The background of the station needs to be neutral, with a recommended lack of accessories on the face.

## Author
* **Satrajit** - [Website](http://satrajit.tk/)

