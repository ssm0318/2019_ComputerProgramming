#include "number.h"
#include "utilities.h"

pair<int, int> Number::fight_result(int A, int B) {
    set<int> FG;
    multiset<int> FA = factorize(A), FB = factorize(B);
    int G;

    set_intersection(FA.begin(), FA.end(), FB.begin(), FB.end(), inserter(FG, FG.begin()));
    if (FG.empty()) {
        G = 1;
    } else {
        G = accumulate(FG.begin(), FG.end(), 1, multiplies<int>());
    }
    return make_pair(A / G, B / G);
}