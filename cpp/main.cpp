// MacOS

#include "tournament.h"

pair<int, int> number_fight(int a, int b) {
    return NumberMatch::virtual_fight(a, b);
}

pair<int, int> number_vs_number(int a, int b) {
    return NumberMatch::one_round(a, b);
}

pair<multiset<int>, multiset<int>> player_battle(
        string type_a, multiset<int> a, string type_b, multiset<int> b
) {
    return PlayerBattle::one_match(type_a, a, type_b, b);
}

pair<multiset<int>, multiset<int>> player_vs_player(
        string type_a, multiset<int> a, string type_b, multiset<int> b
) {
    return PlayerBattle::full_battle(type_a, a, type_b, b);
}

int tournament(vector<pair<string, multiset<int>>> players) {
    return Tournament::proceed_tournament(players);
}

int steady_winner(vector<pair<string, multiset<int>>> players) {
    return Tournament::find_steady_winner(players);
}

/* =======END OF TODOs======= */

/* =======START OF THE MAIN CODE======= */
/* Please do not modify the code below */

typedef pair<string, multiset<int>> player;

player scan_player() {
    multiset<int> numbers;
    string player_type; int size;
    cin >> player_type >> size;
    for(int i=0;i<size;i++) {
        int t; cin >> t; numbers.insert(t);
    }
    return make_pair(player_type, numbers);
}

void print_multiset(const multiset<int>& m) {
    for(int number : m) {
        cout << number << " ";
    }
    cout << endl;
}

int main() {
    int question_number; cin >> question_number;
    if (question_number == 1) {
        int a, b; cin >> a >> b;
        tie(a, b) = number_fight(a, b);
        cout << a << " " << b << endl;
    } else if (question_number == 2) {
        int a, b; cin >> a >> b;
        tie(a, b) = number_vs_number(a, b);
        cout << a << " " << b << endl;
    } else if (question_number == 3 || question_number == 4) {
        auto a = scan_player();
        auto b = scan_player();
        multiset<int> a_, b_;
        if (question_number == 3) {
            tie(a_, b_) = player_battle(
                    a.first, a.second, b.first, b.second
            );
        } else {
            tie(a_, b_) = player_vs_player(
                    a.first, a.second, b.first, b.second
            );
        }
        print_multiset(a_);
        print_multiset(b_);
    } else if (question_number == 5 || question_number == 6) {
        int num_players; cin >> num_players;
        vector<player> players;
        for(int i=0;i<num_players;i++) {
            players.push_back(scan_player());
        }
        int winner_id;
        if (question_number == 5) {
            winner_id = tournament(players);
        } else {
            winner_id = steady_winner(players);
        }
        cout << winner_id << endl;
    }
    return 0;
}
/* =======END OF MAIN CODE======= */