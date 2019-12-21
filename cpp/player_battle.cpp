#include "player_battle.h"

pair<multiset<int>, multiset<int>> PlayerBattle::one_match(string type_a, multiset<int> &a, string type_b, multiset<int> &b) {
    PlayerBattle curr_battle(type_a, a, type_b, b);
    curr_battle.fill_result_matrix();

    curr_battle.index_choice_A = curr_battle.A->make_decision(curr_battle.delta_A_matrix);
    curr_battle.index_choice_B = curr_battle.B->make_decision(curr_battle.delta_B_matrix);

    int num_A = *next(curr_battle.A->get_numbers().begin(), curr_battle.index_choice_A);
    int num_B = *next(curr_battle.B->get_numbers().begin(), curr_battle.index_choice_B);

    auto result = NumberMatch::one_round(num_A, num_B);

    curr_battle.A->update_number(curr_battle.index_choice_A, result.first);
    curr_battle.B->update_number(curr_battle.index_choice_B, result.second);

    return make_pair(curr_battle.A->get_numbers(), curr_battle.B->get_numbers());
}

void PlayerBattle::add_player(const string& player_type, multiset<int> &numbers, Player *&player) {
    if (player_type == MAXIMIZE_GAIN) {
        player = new GainMaximizer(numbers);
    } else if (player_type == MINIMIZE_LOSS) {
        player = new LossMinimizer(numbers);
    } else if (player_type == MINIMIZE_REGRET) {
        player = new RegretMinimizer(numbers);
    }
}

void PlayerBattle::fill_result_matrix() {
    auto itrA = A->get_numbers().begin();
    for (int i = 0; i < N; ++i, ++itrA) {
        auto itrB = B->get_numbers().begin();
        for (int j = 0; j < M; ++j, ++itrB) {
            result_matrix[i][j] = NumberMatch::one_round(*itrA, *itrB);
            delta_A_matrix[i][j] = result_matrix[i][j].first - *itrA;
            delta_B_matrix[j][i] = result_matrix[i][j].second - *itrB;
        }
    }
}

pair<multiset<int>, multiset<int>> PlayerBattle::full_battle(string type_a, multiset<int> &a, string type_b, multiset<int> &b) {
    return pair<multiset<int>, multiset<int>>();
}







/*
 *
 */
void PlayerBattle::print_matrices() {
    cout << "printing result matrix" << endl;
    for (auto row = result_matrix.begin(); row != result_matrix.end(); row++) {
        for (auto col = row->begin(); col != row->end(); col++) {
            cout << "(" << (*col).first << ", " << (*col).second << ")" << "\t";
        }
        cout << endl;
    }

    cout << endl << "printing delta A matrix" << endl;
    for (auto row = delta_A_matrix.begin(); row != delta_A_matrix.end(); row++) {
        for (auto col = row->begin(); col != row->end(); col++) {
            cout << *col << "\t";
        }
        cout << endl;
    }

    cout << endl << "printing delta B matrix" << endl;
    for (auto row = delta_B_matrix.begin(); row != delta_B_matrix.end(); row++) {
        for (auto col = row->begin(); col != row->end(); col++) {
            cout << *col << "\t";
        }
        cout << endl;
    }
}

