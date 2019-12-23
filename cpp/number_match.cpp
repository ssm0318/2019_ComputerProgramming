#include "number_match.h"
#include "utilities.h"

pair<int, int> NumberMatch::virtual_fight(int A, int B) {
    set<int> FG;
    auto FA = factorize(A), FB = factorize(B);

    set_intersection(FA.begin(), FA.end(), FB.begin(), FB.end(), inserter(FG, FG.begin()));
    int G = FG.empty() ? 1 : accumulate(FG.begin(), FG.end(), 1, multiplies<>());

    return make_pair(A / G, B / G);
}

pair<int, int> NumberMatch::one_round(int A, int B) {
    NumberMatch curr_match(A, B);
    curr_match.fill_decision_matrix();

    return curr_match.decision_matrix[curr_match.decision_A()][curr_match.decision_B()];;
}

void NumberMatch::fill_decision_matrix() {
    for (int i = 0; i < MATRIX_SIZE; ++i) {
        for (int j = 0; j < MATRIX_SIZE; ++j) {
            decision_matrix[i][j] = choice_consequence(i, j);
        }
    }
}

pair<int, int> NumberMatch::choice_consequence(int choice_A, int choice_B) {
    if (choice_A == FIGHT && choice_B == FIGHT) {
        return virtual_fight(curr_num.first, curr_num.second);
    } else if (choice_A != FIGHT && choice_B != FIGHT) {
        return curr_num;
    } else return attack(choice_A == FIGHT);
}

pair<int, int> NumberMatch::attack(bool A_is_attacking) {
    // if B is the attacker, swap the elements of the number pair
    auto before_attack = A_is_attacking ? curr_num : make_pair(curr_num.second, curr_num.first);
    auto if_fight = virtual_fight(before_attack.first, before_attack.second);
    int D = before_attack.second - if_fight.second; // damage D

    auto after_attack = factorize(before_attack.second).count(7) ?
            process_damage(true, before_attack, D) : process_damage(false, before_attack, D);

    return A_is_attacking ? after_attack : make_pair(after_attack.second, after_attack.first);
}

pair<int, int> NumberMatch::process_damage(bool has_seven, pair<int, int> numbers, int D) {
    int A = numbers.first;
    int B = numbers.second;

    if (has_seven) {
        A = (A - D / 2) < 1 ? 1 : A - D / 2;
        B = (B - D / 2) < 1 ? 1 : B - D / 2;
    } else {
        B = B - D < 1 ? 1 : B - D;
    }

    return make_pair(A, B);
}

// returns 0 if FIGHT, 1 if FLIGHT
int NumberMatch::decision_A() {
    bool when_B_fights = decision_matrix[0][0].first >= decision_matrix[1][0].first == FIGHT;
    bool when_B_flights = decision_matrix[0][1].first >= decision_matrix[1][1].first == FIGHT;

    return when_B_fights == when_B_flights ? when_B_fights : curr_num.first >= curr_num.second;
}

// returns 0 if FIGHT, 1 if FLIGHT
int NumberMatch::decision_B() {
    bool when_A_fights = decision_matrix[0][0].second >= decision_matrix[0][1].second == FIGHT;
    bool when_A_flights = decision_matrix[1][0].second >= decision_matrix[1][1].second == FIGHT;

    return when_A_fights == when_A_flights ? when_A_fights : curr_num.first <= curr_num.second;
}


