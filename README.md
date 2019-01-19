# recon-attendance

This is a facial recognition program intended to help attendance in school for teachers.

 ********************************************
 Reconattendance - ICS4U1 - final project *
 ********************************************


    _____       _             _ _ _      _____ _           _   _            _
   / ____|     | |           (_|_) |    / ____| |         | | | |          (_)
  | (___   __ _| |_ _ __ __ _ _ _| |_  | |    | |__   __ _| |_| |_ ___ _ __ _  ___  ___
   \___ \ / _` | __| '__/ _` | | | __| | |    | '_ \ / _` | __| __/ _ \ '__| |/ _ \/ _ \
   ____) | (_| | |_| | | (_| | | | |_  | |____| | | | (_| | |_| ||  __/ |  | |  __/  __/
  |_____/ \__,_|\__|_|  \__,_| |_|\__|  \_____|_| |_|\__,_|\__|\__\___|_|  | |\___|\___|
                            _/ |                                          _/ |
                           |__/                                          |__/



      _____                            _   _                 _
     |  __ \                          | | | |               | |
     | |__) |___  ___ ___  _ __   __ _| |_| |_ ___ _ __   __| | __ _ _ __   ___ ___
     |  _  // _ \/ __/ _ \| '_ \ / _` | __| __/ _ \ '_ \ / _` |/ _` | '_ \ / __/ _ \
     | | \ \  __/ (_| (_) | | | | (_| | |_| ||  __/ | | | (_| | (_| | | | | (_|  __/
     |_|  \_\___|\___\___/|_| |_|\__,_|\__|\__\___|_| |_|\__,_|\__,_|_| |_|\___\___|




 * Name:
 *      Satrajit Chatterjee
 *
 * Date:
 *      January 18, 2019
 *
 * Course Code:
 *      ICS4U1 - Period 5 - Semester 1
 *
 * Title:
 *      Reconattendance (A combination of reconnaissance and attendance)
 *
 * Description:
 *
 *
 *      Taking attendance at the beginning of a class is time-consuming, and can take quite long depending on the total
 *      population of the class. With the advent of facial recognition, it is a practical idea to use this to take attendance
 *      automatically when the students walk into the room.
 *
 *      This program does so without using any pre-existing libraries, but using an entirely new algorithm.
 *
 * Features:
 *
 *      In a nutshell, reconattendance's components include:
 *
 *      1. A program which is able to recognize when a face is in range of the camera.
 *      2. Allocate identification, or interest points on a recognized face in order to identify it.
 *      3. Compare the digital data with data in the database in order to check if they are in the database.
 *      4. Add the person’s attendance status to the database automatically.
 *
 *
 *      (i) The program has an extremely unique method of recognition of facial recognition by primary treatment of images
 *      to enhance certain features. Therefore, this approach not only makes the existing features work better, but also allows
 *      future additions of more features of a face without too much effort. This makes the program very versatile.
 *
 *      (ii) All major features protected under a self-defined encryption system with a secure password to ensure students
 *      cannot change any major aspects of the program without teachers' supervision
 *
 *      (iii) Adding of further versions of a face over time to improve results.
 *
 *      (iv) A generally fully functional attendance app with or without the camera support, to a certain extent
 *
 *      (v) A new and innovative way of processing images and using them for training models and tweaking parameters
 *
 *
 * Major Skills:
 *
 *      (i) Object oriented programming - Teachers, Students, Classes, Faces
 *      (2) Image Processing - The whole project is based on image manipulation
 *      (3) Object comparisons
 *      (4) Mathematical innovation
 *      (5) Significant planning skills for development of new algorithms
 *
 *
 * Area of concern:
 *      (i) The program uses a Webcam library, which was impossible to be coded natively in Java, hence it is based on C
 *      code to access hardware. Although tested thoroughly, the webcam interface may fail in specific situations such
 *      as unsupported webcams, which is still fixable through minor tweakings
 *
 *      (ii) The algorithm used in this program is built from the ground up, from scratch. Therefore, it has very minimal
 *      training concerning machine learning, and the mathematical capabilities of the models are incomparable to other projects
 *      such as OpenCV. Therefore, the results may often be false.
 *
 *      (iii) Due to lack of time, the models were not trained well enough, but if given the chance, this program is capable
 *      of performing much better over time.
 *
 *********************************************************************************************************************************

