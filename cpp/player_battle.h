
#ifndef CPP_PLAYER_BATTLE_H
#define CPP_PLAYER_BATTLE_H

#include "player.h"

class PlayerBattle {
private:
    Player *A;
    Player *B;
    int N, M, index_choice_A, index_choice_B;
    vector<vector<pair<int, int>>> result_matrix;
    vector<vector<int>> delta_A_matrix;
    vector<vector<int>> delta_B_matrix;

public:
    PlayerBattle(string type_a, multiset<int> &a, string type_b, multiset<int> &b) {
        add_player(type_a, a, A);
        add_player(type_b, b, B);
        N = A->get_size();
        M = B->get_size();
        result_matrix.resize(N , vector<pair<int, int>>(M));
        delta_A_matrix.resize(N, vector<int>(M));
        delta_B_matrix.resize(M, vector<int>(N));
    }
    static pair<multiset<int>, multiset<int>> one_match(string, multiset<int> &, string, multiset<int> &);
    static void add_player(const string&, multiset<int> &, Player *&);
    void fill_result_matrix();
    static int calculate_sum(multiset<int> &);
    static pair<multiset<int>, multiset<int>> full_battle(string, multiset<int> &, string, multiset<int> &);

    void print_matrices();

    ~PlayerBattle() {
        delete A;
        delete B;
    }
};

#endif //CPP_PLAYER_BATTLE_H
