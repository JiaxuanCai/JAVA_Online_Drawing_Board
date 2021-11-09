# [JAVA_GUI_File_Manager](https://github.com/JiaxuanCai/JAVA_GUI_File_Manager)

Spring 2019-2020 JAVA Programming course homeworks -- Chongqing University. Include my source codes and reports.

#### Contents:

1. Draw on the class architecture of Experiment 1 to construct a set of drawing classes, including Shape, a derived class of Shape and graphics class;

2. OpenGLApp of Experiment 1 was changed to a derived class of Jframe class, including Graphics class instance as data member, and a specific layout manager was used to build the main window. Corresponding controls were created in the window to allow users to select the shape, line color and fill color of the currently drawn graph.

3. Add corresponding event monitoring and corresponding methods (processing user input), such as the shape of the drawing graph, line color, fill color selection change;

4. Reload the paintComponent (Graphics G) method of the main window, which is used to draw the Graphics class instance data in the main window. During the drawing process, call the draw() method of these Graphics data, and you can pass the G of the main window to the draw method of each Graphics element class.

5. Perform related processing methods in the MouseListener interface registered in the main window. According to the current user's choice, implement functions similar to the Painter tool in the Window attachment, such as drawing the specified graph and moving the graph.

6. Using SOCK and multithreading mechanism, modify program structure to realize the design and implementation of a Network whiteboard program based on Java SOCK (multi-user cooperative whiteboard drawing program).

#### Design of Classes

Using the class architecture of Experiment 1 as reference, we construct the drawing class set, shape, the derived class of Shape and graphics class. On this basis, Client, Server and SocketThread classes for network connection and image transmission are added. The design of the class follows the MVC design specification, dependency inversion specification and single function specification. The overview diagram of package and class is as follows:

##### (1) Shape

IsSelected is an abstract class that contains a Boolean variable, isSelected, that marks whether the graph isSelected or not;

Abstract methods draw() is used to output the graph and getCN() is used to return the graph category. The selected method determines whether the graph is selected. The print method is used to output information to the command line during debugging. All subclasses that inherit from Shape must implement these methods.



##### (2) Myline class

Public int aX,aY,bX,bY; public int ps; They represent the xy coordinates of the two vertices of the line, and the selected points on the line.

Member function: The draw method uses the position information of the object itself to draw the final image on the artboard; The selected method is used to receive the selection box and determine whether the current line is in the selection box and which point is selected by the selection box. The print method is used to print information to the command line. CN() is used to return the type of the graph; The x and y methods return the position information of the current moment line.



##### (3) MyRectangle class:

Public int x,y,width,height;

public boolean isFilled;

public int ps; Represents the rectangle's vertex XY coordinates, width and height, whether the marker is filled, and the point at which the rectangle is selected.

Member function: The draw method uses the position information of the object itself to draw the final image on the artboard; The selected method is used to receive the selection box and determine whether the current rectangle is in the selection box and which point is selected by the selection box. The print method is used to print information to the command line. CN() is used to return the type of the graph; The x and y methods return the position of the current rectangle.



##### MyOval class:

Public int x,y,width,height;

public boolean isFilled; Represents the vertex xy coordinates of the ellipse, the width and height, and whether the mark is filled.

Member function: The draw method uses the position information of the object itself to draw the final image on the artboard; The selected method is used to receive the selection box and determine whether the current ellipse is in the selection box. The print method is used to print information to the command line. CN() is used to return the type of the graph; The x and y methods return the position of the current ellipse.



##### (5) MyTriangle class:

Public int ax,ay,bx,by,cx,cy;

public boolean isFilled;

public int ps; Each represents the xy coordinates of the three vertices of the triangle, whether the marker is filled, and the selected point of the triangle.

Member function: The draw method uses the position information of the object itself to draw the final image on the artboard; The selected method is used to receive the selection box and determine whether the current triangle is in the selection box and which point is selected by the selection box. The print method is used to print information to the command line. CN() is used to return the type of the graph; The x and y methods return the position information of the current triangle.



##### (6) Graphic class

Performs most of the operations stored in graphics for processing.

Maintains a List of shapes for storing graphs.

Maintains a MyRectangle class variable chooseField to store selection box objects.

Maintain four integer oldMouseX oldMouseY, nowMouseX, nowMouseY, X, Y, is used to monitor the location of the mouse, the location of the updated graphics.

Override the paint method to draw in real time the intermediate process diagram of the current drawing, and the selection box in real time if in selection mode. Make the experience of drawing very smooth and coherent.

Implement addShape method, which is used to determine the final position information of the graph being drawn when the mouse is released, and then use this position information to call the constructor of the corresponding graph to construct the graph object, and store it in the Shapes table. Also, in select mode, the position of the select box will be updated when the mouse is released.

Implement selectShape method, in the selection mode, using our selection box as a parameter, to determine whether each graph object in the List is located in the selection box. It should be noted that, if mode is not selected, each graph object is left unchecked to conform to our design logic.

Implement the delete method so that we can delete the currently selected graph by clicking the Delete button in select mode.

The change method is implemented so that we can modify the selected graph in the selection mode.

Implement the setStroke method to update the brush thickness in real time.

The MousePressed, MouseREleased, and MouseDragged methods will be overhauled, update the position of the mouse, and call the appropriate method.



##### (7)OpenGLApp class:

Acts as a controller class that implements the main interface and implements all operations that interact with the user.

The object place of the Graphic class is maintained as the main interface for drawing.

Maintain a lot of attribute variables, used to control the drawing color, brush thickness and other attributes, used to control the transmission of information and other operations.

In the main interface, JAVA Swing is used for GUI layout, making the interface simple and beautiful, and the operation logic is clear. The vector icon used in button construction comes from Alibaba's open source vector icon library IconsFront.

Multiple listeners are implemented to monitor the click of each button.



##### (8) Client class:

The initial construction of the client, the client's service start function StartClient, the function ClientRecive to receive the transmitted data, and the client's image sending function ClientImageSend.



##### (9) Server class:

The initial construction of the client and the StartServer function of the client are realized.



##### (10)SocketThread class:

The initial construction of the client, the client's service start function StartClient, the function ServerRecive receiving transmission data, and the client's image sending function ServerImageSend function are realized.

#### Design of Process

In the OpenGLApp class, the Main function is the entry to the program;

The OpenGLApp Graphic member variable can be initialized by calling the constructor of the OpenGLApp class from the Main function. After initialization, you can operate on it. You can instantiate some of the various Shape class objects and then use the Add method to add them to the Shape array of the Graphic class object for subsequent operations. You can call the paint method in the Main function to draw all the images, or you can call the change method in the Main function to update the object.



See codes and corresponding lab reports in the folder of each lab project.
