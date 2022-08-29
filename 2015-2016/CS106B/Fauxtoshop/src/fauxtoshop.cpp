/*
* Assignment 1, Fauxtoshop, CS106B
* --------------------------------
* This program ostensibly is a programming student's homework to
* implement a completely dumbed down Photoshop, only
* incorporating basic filters like scatter and edge detection.
*
*/

#include <iostream>
#include <cmath>
#include "console.h"
#include "gwindow.h"
#include "grid.h"
#include "simpio.h"
#include "strlib.h"
#include "gbufferedimage.h"
#include "gevents.h"
#include "math.h" //for sqrt and exp in the optional Gaussian kernel
#include "random.h"
using namespace std;

static const int    WHITE = 0xFFFFFF;
static const int    BLACK = 0x000000;
static const int    GREEN = 0x00FF00;
static const double PI    = 3.14159265;

static void     doFauxtoshop(GWindow &gw, GBufferedImage &img);

static bool     openImageFromFilename(GBufferedImage& img, string filename);
static bool 	saveImageToFilename(const GBufferedImage &img, string filename);
static void     getMouseClickLocation(int &row, int &col);

static void     printMenu();
static int      getRandomPixel(int radius, Grid<int> &originalImage, int row, int col);
static bool     isEdge(int row, int col, Grid<int> &originalImage, int threshold);
static int      getPixelDifference(int row1, int col1, int row2, int col2, Grid<int> &gridImage);
static int      greenDifference(int pixel, int greenColor);
/* STARTER CODE FUNCTION - DO NOT EDIT
 *
 * This main simply declares a GWindow and a GBufferedImage for use
 * throughout the program. By asking you not to edit this function,
 * we are enforcing that the GWindow have a lifespan that spans the
 * entire duration of execution (trying to have more than one GWindow,
 * and/or GWindow(s) that go in and out of scope, can cause program
 * crashes).
 */
int main() {
    GWindow gw;
    gw.setTitle("Fauxtoshop");
    gw.setVisible(true);
    GBufferedImage img;
    doFauxtoshop(gw, img);
    return 0;
}

/*
* Main console-based interface for the user.
*/
static void doFauxtoshop(GWindow &gw, GBufferedImage &img) {
    bool quitFlag = false;
    while(true){
        cout << "Welcome to Fauxtoshop!" << endl;

        while(true) {
            string filename = getLine("Enter the name of a file to open (or blank to quit): ");
            if(filename == ""){
                quitFlag = true;
                break;
            }else if(openImageFromFilename(img, filename)){
                break;
            }else{
               cout << "Error." << endl;
            }
        }
        if(quitFlag){
            break;
        }

        gw.setCanvasSize(img.getWidth(), img.getHeight());
        gw.add(&img,0,0);

        printMenu();
        int filterChoice = -1;
        while(filterChoice < 1 || filterChoice > 4) {
            filterChoice = getInteger("Your choice: ");
        }

        Grid<int> originalImage = img.toGrid();
        Grid<int> newImage(originalImage.numRows(), originalImage.numCols());
        if(filterChoice == 1) {
            int radius = -1;
            while(radius < 1 || radius > 100) {
                radius = getInteger("Enter degree of scatter [1-100]: ");
            }
            for(int row = 0; row < newImage.numRows(); row++) {
                for(int col = 0; col < newImage.numCols(); col++) {
                    newImage[row][col] = getRandomPixel(radius, originalImage, row, col);
                }
            }
        }else if(filterChoice == 2){
            int threshold = -1;
            while(threshold < 1){
                threshold = getInteger("Enter a threshold for edges: ");
            }

            for(int row = 0; row < newImage.numRows(); row++){
                for(int col = 0; col < newImage.numCols(); col++){
                    if(isEdge(row, col, originalImage, threshold)) {
                        newImage[row][col] = BLACK;
                    }else{
                        newImage[row][col] = WHITE;
                    }
                }
            }
        }else if(filterChoice == 3){
            Grid<int> stickerGrid;
            int lessGreenThreshold;
            while(true){
                string filename = getLine("Enter an image file to paste on background: ");
                GBufferedImage stickerImg;
                if(openImageFromFilename(stickerImg, filename)){
                    stickerGrid = stickerImg.toGrid();
                    break;
                }
            }
            while(true){
                lessGreenThreshold = getInteger("Enter integer tolerance for green screen (higher for less green): ");
                if(lessGreenThreshold >= 1){
                    break;
                }
            }

            int rowCoord;
            int colCoord;
            while(true){
                string stringCoords = getLine("Enter location to place image as (row,col) or blank line to use mouse: ");
                if(stringCoords == ""){
                    cout << "Now click somewhere on the background image: " << endl;
                    getMouseClickLocation(rowCoord, colCoord);
                    cout << "You clicked " << "(" << rowCoord << "," << colCoord << ")" << endl;
                    break;
                }
                rowCoord = stringToInteger(stringCoords.substr(stringCoords.find("(") + 1, stringCoords.find(",") - 1));
                colCoord = stringToInteger(stringCoords.substr(stringCoords.find(",") + 1, stringCoords.find(")") - 1));
                if(rowCoord >= 0 && colCoord >= 0) break;
            }

            newImage = originalImage;
            for(int i = rowCoord; i < newImage.numRows(); i++){
                for(int j = colCoord; j < newImage.numCols(); j++){
                    int stickerRow = i - rowCoord;
                    int stickerCol = j - colCoord;
                    if(stickerRow >= stickerGrid.numRows() || stickerCol >= stickerGrid.numCols()) break;
                    if(greenDifference(stickerGrid[stickerRow][stickerCol], GREEN) > lessGreenThreshold){
                        newImage[i][j] = stickerGrid[stickerRow][stickerCol];
                    }
                }
            }

        }else if(filterChoice == 4){
            GBufferedImage otherImage;
            newImage = originalImage;
            while(true){
                string filename = getLine("Enter an image to compare: ");
                if(openImageFromFilename(otherImage, filename)){
                    break;
                }
            }
            int diffPixels = img.countDiffPixels(otherImage);
            if(diffPixels != 0) {
                cout << "These images differ in " << diffPixels << " locations"<< endl;
            }else{
                cout << "These images are the same!" << endl;
            }


        }

        img.fromGrid(newImage);

        while(true) {
            string saveFilename = getLine("Enter filename to save image (or blank to skip saving): ");
            if(saveFilename == "") {
                break;
            }
            if(saveImageToFilename(img, saveFilename)) {
                break;
            }
            cout << "Error." << endl;
        }

        gw.clear();
        cout << endl;
    }
}

/*
 * Returns an integer given a green value and pixel RGB values
 * indicating the difference using the same edge detection algorithm.
*/
static int greenDifference(int pixel, int greenColor){
    int pRed, pGreen, pBlue;
    GBufferedImage::getRedGreenBlue(pixel, pRed, pGreen, pBlue);

    int gRed, gGreen, gBlue;
    GBufferedImage::getRedGreenBlue(greenColor, gRed, gGreen, gBlue);

    int diffRed = abs(pRed - gRed);
    int diffGreen = abs(pGreen - gGreen);
    int diffBlue = abs(pBlue - gBlue);

    int maxOfRGB = max(diffBlue, (max(diffRed, diffGreen)));
    return maxOfRGB;
}

/*
* Determines whether the given pixel in the row/col is an edge
* given a threshold.
*/
static bool isEdge(int row, int col, Grid<int> &originalImage, int threshold) {
    for(int i = -1; i <= 1; i++){
        for(int j = -1; j <= 1; j++){
            int neighborRow = row + i;
            int neighborCol = col + j;
            if(originalImage.inBounds(neighborRow, neighborCol)){
                if(getPixelDifference(row, col, neighborRow, neighborCol, originalImage)
                        > threshold){
                    return true;
                }
            }
        }
    }
    return false;
}

/*
* Algorithm that finds an integer representing difference between two pixels.
*/
static int getPixelDifference(int row1, int col1, int row2, int col2, Grid<int> &gridImage){
    int pixel1 = gridImage[row1][col1];
    int red1, green1, blue1;
    GBufferedImage::getRedGreenBlue(pixel1, red1, green1, blue1);

    int pixel2 = gridImage[row2][col2];
    int red2, green2, blue2;
    GBufferedImage::getRedGreenBlue(pixel2, red2, green2, blue2);

    int diffRed = abs(red1 - red2);
    int diffGreen = abs(green1 - green2);
    int diffBlue = abs(blue1 - blue2);

    int maxOfRGB = max(diffBlue, (max(diffRed, diffGreen)));
    return maxOfRGB;
}

/*
* Given a "radius" and a grid of pixel rgb values,
* returns a random pixel value around the area of the radius around
* the cell in the row and coloumn of the grid.
*/
static int getRandomPixel(int radius, Grid<int> &originalImage, int row, int col){
    int randomRow = -1;
    int randomCol = -1;
    while(!originalImage.inBounds(randomRow, randomCol)){
        randomRow = randomInteger(-radius, radius) + row;
        randomCol = randomInteger(-radius, radius) + col;
    }
    return originalImage[randomRow][randomCol];
}

/*
* Prints menu of options.
*/
static void printMenu() {
    cout << "Which image filter would you like to apply?" << endl;
    cout << "       1 - Scatter" << endl;
    cout << "       2 - Edge detection" << endl;
    cout << "       3 - \"Green screen\" with another image" << endl;
    cout << "       4 - Compare image with another image" << endl;
}

/* STARTER CODE HELPER FUNCTION - DO NOT EDIT
 *
 * Attempts to open the image file 'filename'.
 *
 * This function returns true when the image file was successfully
 * opened and the 'img' object now contains that image, otherwise it
 * returns false.
 */
static bool openImageFromFilename(GBufferedImage& img, string filename) {
    try { img.load(filename); }
    catch (...) { return false; }
    return true;
}

/* STARTER CODE HELPER FUNCTION - DO NOT EDIT
 *
 * Attempts to save the image file to 'filename'.
 *
 * This function returns true when the image was successfully saved
 * to the file specified, otherwise it returns false.
 */
static bool saveImageToFilename(const GBufferedImage &img, string filename) {
    try { img.save(filename); }
    catch (...) { return false; }
    return true;
}

/* STARTER CODE HELPER FUNCTION - DO NOT EDIT
 *
 * Waits for a mouse click in the GWindow and reports click location.
 *
 * When this function returns, row and col are set to the row and
 * column where a mouse click was detected.
 */
static void getMouseClickLocation(int &row, int &col) {
    GMouseEvent me;
    do {
        me = getNextEvent(MOUSE_EVENT);
    } while (me.getEventType() != MOUSE_CLICKED);
    row = me.getY();
    col = me.getX();
}

/* OPTIONAL HELPER FUNCTION
 *
 * This is only here in in case you decide to impelment a Gaussian
 * blur as an OPTIONAL extension (see the suggested extensions part
 * of the spec handout).
 *
 * Takes a radius and computes a 1-dimensional Gaussian blur kernel
 * with that radius. The 1-dimensional kernel can be applied to a
 * 2-dimensional image in two separate passes: first pass goes over
 * each row and does the horizontal convolutions, second pass goes
 * over each column and does the vertical convolutions. This is more
 * efficient than creating a 2-dimensional kernel and applying it in
 * one convolution pass.
 *
 * This code is based on the C# code posted by Stack Overflow user
 * "Cecil has a name" at this link:
 * http://stackoverflow.com/questions/1696113/how-do-i-gaussian-blur-an-image-without-using-any-in-built-gaussian-functions
 *
 */
static Vector<double> gaussKernelForRadius(int radius) {
    if (radius < 1) {
        Vector<double> empty;
        return empty;
    }
    Vector<double> kernel(radius * 2 + 1);
    double magic1 = 1.0 / (2.0 * radius * radius);
    double magic2 = 1.0 / (sqrt(2.0 * PI) * radius);
    int r = -radius;
    double div = 0.0;
    for (int i = 0; i < kernel.size(); i++) {
        double x = r * r;
        kernel[i] = magic2 * exp(-x * magic1);
        r++;
        div += kernel[i];
    }
    for (int i = 0; i < kernel.size(); i++) {
        kernel[i] /= div;
    }
    return kernel;
}
