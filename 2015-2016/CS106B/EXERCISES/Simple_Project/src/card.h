#ifndef CARD_H
#define CARD_H

class Card {
public:
    Card(int r, int s);
    int getRank(Card card);
private:
    int rank;
    int suit;
};

#endif

