//
// breakout.c
//
// Computer Science 50
// Problem Set 3
//

// standard libraries
#define _XOPEN_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// Stanford Portable Library
#include <spl/gevents.h>
#include <spl/gobjects.h>
#include <spl/gwindow.h>

// height and width of game's window in pixels
#define HEIGHT 600
#define WIDTH 400

// number of rows of bricks
#define ROWS 5

// number of columns of bricks
#define COLS 10

// radius of ball in pixels
#define RADIUS 8 // 10

// lives
#define LIVES 3

// height and width of paddle
#define PADDLEW  57
#define PADDLEH 10

// height and width of blocks
#define BLOCKW 35
#define BLOCKH 9
 
// changes made to non-TODO
// char[12] 
// RADIUS 10

// prototypes
void initBricks(GWindow window);
GOval initBall(GWindow window);
GRect initPaddle(GWindow window);
GLabel initScoreboard(GWindow window);
void updateScoreboard(GWindow window, GLabel label, int points);
GObject detectCollision(GWindow window, GOval ball);

int main(void)
{
    // seed pseudorandom number generator
    srand48(time(NULL));

    // instantiate window
    GWindow window = newGWindow(WIDTH, HEIGHT);

    // instantiate bricks
    initBricks(window);

    // instantiate ball, centered in middle of window
    GOval ball = initBall(window);

    // instantiate paddle, centered at bottom of window
    GRect paddle = initPaddle(window);

    // instantiate scoreboard, centered in middle of window, just above ball
    GLabel label = initScoreboard(window);

    // number of bricks initially
    int bricks = COLS * ROWS;

    // number of lives initially
    int lives = LIVES;

    // number of points initially
    int points = 0;

    // keep playing until game over
    waitForClick();
    while (lives > 0 && bricks > 0)
    {
        // TODO     
        double velocity = drand48() + 1.2;
        double velocity_y = 2.7;
        int x = 0;
        
        while (x == 0)
        {
            GEvent mouse = getNextEvent(MOUSE_EVENT);
            if (mouse != NULL)
            {
                if (getEventType(mouse) == MOUSE_MOVED)
                {
                    double x = getX(mouse) - (getWidth(paddle) / 2);
                    double y = getHeight(window) - 62;
                    setLocation(paddle, x, y);
                }
            } 
            move(ball, velocity, velocity_y);
            GObject object = detectCollision(window, ball);
            if (object != NULL)
            {    
                if (getY(ball) <= 0 || object == paddle)
                {
                    velocity_y = -velocity_y;
                } 
                else if (getX(ball) <= 0)
                {
                    velocity = -velocity;
                }   
                else if(getY(ball) + getHeight(ball) >= getHeight(window))
                {
                    lives = lives - 1;                   
                    x = x + 1;                    
                    double x = (getWidth(window) / 2) - (getWidth(ball) / 2);
                    double y = (getHeight(window) / 2) - (getHeight(ball) / 2) + 20;
                    setLocation(ball, x, y);
                    x = x - 1;         
                } 
                else if(getX(ball) + getWidth(ball) >= getWidth(window))
                {
                    velocity = -velocity;
                }
                else if(strcmp(getType(object), "GRect") == 0)
                {
                    // bricks = bricks - 1;
                    velocity_y = -velocity_y;
                    removeGWindow(window, object);                   
                    updateScoreboard(window, label, points = points + 10);
                         
                }
                if (points == 500)
                {
                    GLabel winner = newGLabel("W1N !_!");
                    setFont(winner, "SansSerif-26");
                    setColor(winner, "GREEN");
                    add(window, winner);
                    double x = getWidth(window) / 2 - getWidth(winner) / 2;
                    double y = getHeight(window) / 2 + 55;
                    setLocation(winner, x, y);
                    waitForClick();
                    closeGWindow(window);
                    return 0;
                }
            }
            pause(8);       
        }
    }

    // wait for click before exiting
    // lose by default
    GLabel loser = newGLabel("L0SE ~_~");
    setFont(loser, "SansSerif-22");
    setColor(loser, "RED");
    add(window, loser);
    double x = getWidth(window) / 2 - getWidth(loser) / 2;
    double y = getHeight(window) / 2 + 55;
    setLocation(loser, x, y);
    waitForClick();

    // game over
    closeGWindow(window);
    return 0;
}

/**
 * Initializes window with a grid of bricks.
 */
void initBricks(GWindow window)
{
    // TODO
    for (int i = 0; i < ROWS; i++)
    {
        for (int k = 0; k < COLS; k++)
        {
            GRect rekt_m8 = newGRect(2 + (k * (BLOCKW + 5)), 55 + (i * (BLOCKH + 5)), BLOCKW, BLOCKH);
            setFilled(rekt_m8, true);
            if (i == 0)
            {
                setColor(rekt_m8, "MAGENTA");
            }
            else if (i == ROWS - 1)
            {
                setColor(rekt_m8, "MAGENTA");
            }
            else if (i == 2)
            {
                setColor(rekt_m8, "ORANGE");
            }
            else
            {
                setColor(rekt_m8, "GREEN");
            }
            add(window, rekt_m8);
        }   
    }
}

/**
 * Instantiates ball in center of window.  Returns ball.
 */
GOval initBall(GWindow window)
{
    // TODO
    GOval ball = newGOval(0, 0, 20, 20);
    double x = (getWidth(window) / 2) - (getWidth(ball) / 2);
    double y = (getHeight(window) / 2) - (getHeight(ball) / 2) + 20;
    setLocation(ball, x, y);
    setFilled(ball, true);
    setColor(ball, "PINK");
    add(window, ball);
    return ball;
}

/**
 * Instantiates paddle in bottom-middle of window.
 */
GRect initPaddle(GWindow window)
{
    // TODO
    GRect paddle = newGRect(((getWidth(window)) / 2) - (PADDLEW / 2), getHeight(window) - 62, PADDLEW, PADDLEH);
    setColor(paddle, "DARK_GRAY");
    setFilled(paddle, true);
    add(window, paddle);
    return paddle;
    
}

/**
 * Instantiates, configures, and returns label for scoreboard.
 */
GLabel initScoreboard(GWindow window)
{
    // TODO
    GLabel label = newGLabel("0");
    setFont(label, "SansSerif-36");
    setColor(label, "LIGHT_GRAY");
    add(window, label);
    double x = getWidth(window) / 2 - getWidth(label) / 2;
    double y = getHeight(window) / 2;
    setLocation(label, x, y);
    return label;
    
}

/**
 * Updates scoreboard's label, keeping it centered in window.
 */
void updateScoreboard(GWindow window, GLabel label, int points)
{
    // update label
    char s[4];
    sprintf(s, "%i", points);
    setLabel(label, s);

    // center label in window
    double x = (getWidth(window) - getWidth(label)) / 2;
    double y = (getHeight(window) - getHeight(label)) / 2;
    setLocation(label, x, y);
}

/**
 * Detects whether ball has collided with some object in window
 * by checking the four corners of its bounding box (which are
 * outside the ball's GOval, and so the ball can't collide with
 * itself).  Returns object if so, else NULL.
 */
GObject detectCollision(GWindow window, GOval ball)
{
    // ball's location
    double x = getX(ball);
    double y = getY(ball);

    // for checking for collisions
    GObject object;

    // check for collision at ball's top-left corner
    object = getGObjectAt(window, x, y);
    if (object != NULL)
    {
        return object;
    }

    // check for collision at ball's top-right corner
    object = getGObjectAt(window, x + 2 * RADIUS, y);
    if (object != NULL)
    {
        return object;
    }

    // check for collision at ball's bottom-left corner
    object = getGObjectAt(window, x, y + 2 * RADIUS);
    if (object != NULL)
    {
        return object;
    }

    // check for collision at ball's bottom-right corner
    object = getGObjectAt(window, x + 2 * RADIUS, y + 2 * RADIUS);
    if (object != NULL)
    {
        return object;
    }

    // no collision
    return NULL;
}
