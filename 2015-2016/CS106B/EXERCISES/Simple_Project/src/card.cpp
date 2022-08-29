#include "card.h"

Card::Card(int r, int s) {
    rank = r;
    suit = s;
}

int Card::getRank(Card card) {
    return card.rank;
}
