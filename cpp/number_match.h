
#ifndef CPP_NUMBER_MATCH_H
#define CPP_NUMBER_MATCH_H

#include <iterator>
#include <algorithm>
#include <numeric>
#include "env.h"

class NumberMatch {
private:
    static const int FIGHT = 0;
    static const int MATRIX_SIZE = 2;

    pair<int, int> curr_num;
    pair<int, int> decision_matrix[MATRIX_SIZE][MATRIX_SIZE];

public:
    NumberMatch(int A, int B) : curr_num(make_pair(A, B)) {}

    static pair<int, int> virtual_fight(int, int);
    static pair<int, int> one_match(int, int);
    void fill_decision_matrix();
    pair<int, int> attack(bool);
    static pair<int, int> process_damage(bool, pair<int, int>, int);
    pair<int, int> res_choice(int, int);
    int decision_A();
    int decision_B();
};

#endif //CPP_NUMBER_MATCH_H
