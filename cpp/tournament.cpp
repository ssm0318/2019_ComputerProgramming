#include "tournament.h"

deque<tuple<int, string, multiset<int>>> convert_to_queue(vector<pair<string, multiset<int>>> &players) {
    deque<tuple<int, string, multiset<int>>> queue;
    int id = 0;
    
    for (auto el : players) {
        queue.push_back(make_tuple(id++, el.first, el.second));
    }

    return queue;
}

int Tournament::tournament_winner(deque<tuple<int, string, multiset<int>>> &players_queue) {
    while (players_queue.size() > 1) {
        auto A = players_queue.front();
        players_queue.pop_front();
        auto B = players_queue.front();
        players_queue.pop_front();
        if (get<0>(B) < get<0>(A)) { // handling last player in case num players is odd
            players_queue.push_front(A);
            players_queue.push_back(B);
            continue;
        }
        auto res = PlayerBattle::full_battle(get<1>(A), get<2>(A), get<1>(B), get<2>(B));
        int res_A = PlayerBattle::calculate_sum(res.first);
        int res_B = PlayerBattle::calculate_sum(res.second);
        res_A >= res_B ? players_queue.push_back(A) : players_queue.push_back(B);
    }

    return get<0>(players_queue.front());
}

int Tournament::proceed_tournament(vector<pair<string, multiset<int>>> &players) {
    auto players_queue = convert_to_queue(players);

    return tournament_winner(players_queue);
}

int Tournament::find_steady_winner(vector<pair<string, multiset<int>>> &players) {
    vector<int> count_wins(players.size(), 0);
    auto tournament_queue = convert_to_queue(players);

    for (int i = 0; i < players.size(); ++i) {
        count_wins[tournament_winner(tournament_queue)]++;
        auto temp_player = tournament_queue.front();
        tournament_queue.pop_front();
        tournament_queue.push_back(temp_player);
    }

    return distance(count_wins.begin(), max_element(count_wins.begin(), count_wins.end()));
}
