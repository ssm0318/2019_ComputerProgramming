#include <iostream>
#include <string>
#include <utility>
#include <set>
#include <vector>
#include <tuple>

using namespace std;

/* =======START OF PRIME-RELATED HELPERS======= */
/*
 * The code snippet below AS A WHOLE does the primality
 * test and integer factorization. Feel free to move the
 * code to somewhere more appropriate to get your codes
 * more structured.
 *
 * You don't have to understand the implementation of it.
 * But if you're curious, refer to the sieve of Eratosthenes
 *
 * If you want to just use it, use the following 2 functions.
 *
 * 1) bool is_prime(int num):
 *     * `num` should satisfy 1 <= num <= 999999
 *     - returns true if `num` is a prime number
 *     - returns false otherwise (1 is not a prime number)
 *
 * 2) multiset<int> factorize(int num):
 *     * `num` should satisfy 1 <= num <= 999999
 *     - returns the result of factorization of `num`
 *         ex ) num = 24 --> result = { 2, 2, 2, 3 }
 *     - if `num` is 1, it returns { 1 }
 */

const int PRIME_TEST_LIMIT = 999999;
int sieve_of_eratosthenes[PRIME_TEST_LIMIT + 1];
bool sieve_calculated = false;

void make_sieve() {
    sieve_of_eratosthenes[0] = -1;
    sieve_of_eratosthenes[1] = -1;
    for(int i=2; i<=PRIME_TEST_LIMIT; i++) {
        sieve_of_eratosthenes[i] = i;
    }
    for(int i=2; i*i<=PRIME_TEST_LIMIT; i++) {
        if(sieve_of_eratosthenes[i] == i) {
            for(int j=i*i; j<=PRIME_TEST_LIMIT; j+=i) {
                sieve_of_eratosthenes[j] = i;
            }
        }
    }
    sieve_calculated = true;
}

bool is_prime(int num) {
    if (!sieve_calculated) {
        make_sieve();
    }
    return sieve_of_eratosthenes[num] == num;
}

multiset<int> factorize(int num) {
    if (!sieve_calculated) {
        make_sieve();
    }
    multiset<int> result;
    while(num > 1) {
        result.insert(sieve_of_eratosthenes[num]);
        num /= sieve_of_eratosthenes[num];
    }
    if(result.empty()) {
        result.insert(1);
    }
    return result;
}

/* =======END OF PRIME-RELATED HELPERS======= */

/* =======START OF STRING LITERALS======= */
/* Use this code snippet if you want */

const string MAXIMIZE_GAIN = "Maximize-Gain";
const string MINIMIZE_LOSS = "Minimize-Loss";
const string MINIMIZE_REGRET = "Minimize-Regret";

/* =======END OF STRING LITERALS======= */


/* =======START OF TODOs======= */

pair<int, int> number_fight(int a, int b) {
    // TODO 1-1
    return pair<int, int>();
}

pair<int, int> number_vs_number(int a, int b) {
    // TODO 1-2
    return pair<int, int>();
}

pair<multiset<int>, multiset<int>> player_battle(
    string type_a, multiset<int> a, string type_b, multiset<int> b
) {
    // TODO 1-3
    return pair<multiset<int>, multiset<int>>();
}

pair<multiset<int>, multiset<int>> player_vs_player(
    string type_a, multiset<int> a, string type_b, multiset<int> b
) {
    // TODO 1-4
    return pair<multiset<int>, multiset<int>>();
}

int tournament(vector<pair<string, multiset<int>>> players) {
    // TODO 1-5
    return -1;
}

int steady_winner(vector<pair<string, multiset<int>>> players) {
    // TODO 1-6
    return -1;
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