
#ifndef CPP_NUMBER_MATCH_H
#define CPP_NUMBER_MATCH_H

#include <iterator>
#include <algorithm>
#include <numeric>
#include <array>
#include "env.h"

class NumberMatch {
private:
    static const int FIGHT = 0;
    static const int MATRIX_SIZE = 2;

    pair<int, int> curr_num;
    array<array<pair<int, int>, MATRIX_SIZE>, MATRIX_SIZE> decision_matrix;

public:
    NumberMatch(int A, int B) : curr_num(make_pair(A, B)) {}

    static pair<int, int> virtual_fight(int, int);
    static pair<int, int> one_round(int, int);
    void fill_decision_matrix();
    pair<int, int> attack(bool);
    static pair<int, int> process_damage(bool, pair<int, int>, int);
    pair<int, int> choice_consequence(int, int);
    int decision_A();
    int decision_B();
};

#endif //CPP_NUMBER_MATCH_H
