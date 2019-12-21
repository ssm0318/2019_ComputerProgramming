
#ifndef CPP_TOURNAMENT_H
#define CPP_TOURNAMENT_H

#include "player_battle.h"

class Tournament {
public:
    static int proceed_tournament(vector<pair<string, multiset<int>>> &);
    static int tournament_winner(deque<tuple<int, string, multiset<int>>> &);
    static int find_steady_winner(vector<pair<string, multiset<int>>> &);
};

#endif //CPP_TOURNAMENT_H
