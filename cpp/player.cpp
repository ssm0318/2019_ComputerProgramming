#include <deque>
#include "player.h"

string GainMaximizer::type = MAXIMIZE_GAIN;
string LossMinimizer::type = MINIMIZE_LOSS;
string RegretMinimizer::type = MINIMIZE_REGRET;

int GainMaximizer::get_size() { return numbers.size(); }
int LossMinimizer::get_size() { return numbers.size(); }
int RegretMinimizer::get_size() { return numbers.size(); }

multiset<int> & GainMaximizer::get_numbers() { return numbers; }
multiset<int> & LossMinimizer::get_numbers() { return numbers; }
multiset<int> & RegretMinimizer::get_numbers() { return numbers; }

void GainMaximizer::update_number(int index, int num) {
    auto iter = next(numbers.begin(), index);
    numbers.erase(iter);
    numbers.insert(num);
}

void LossMinimizer::update_number(int index, int num) {
    auto iter = next(numbers.begin(), index);
    numbers.erase(iter);
    numbers.insert(num);
}

void RegretMinimizer::update_number(int index, int num) {
    auto iter = next(numbers.begin(), index);
    numbers.erase(iter);
    numbers.insert(num);
}

int GainMaximizer::make_decision(vector<vector<int>> &delta_matrix) {
    vector<int> max;
    for (auto row = delta_matrix.begin(); row != delta_matrix.end(); ++row) {
        max.push_back(*max_element(row->begin(), row->end()));
    }

    return distance(max.begin(), max_element(max.begin(), max.end()));
}

int LossMinimizer::make_decision(vector<vector<int>> &delta_matrix) {
    vector<int> min;
    for (auto row = delta_matrix.begin(); row != delta_matrix.end(); ++row) {
        min.push_back(*min_element(row->begin(), row->end()));
    }

    return distance(min.begin(), max_element(min.begin(), min.end()));
}

deque<int> convert_matrix(vector<vector<int>> &delta_matrix) {
    deque<int> delta_queue;
    for (auto row = delta_matrix.begin(); row != delta_matrix.end(); row++) {
        for (auto col = row->begin(); col != row->end(); col++) {
            delta_queue.push_back(*col);
        }
    }
    return delta_queue;
}

void print_queue(deque<int> &dd) {
    cout << "printing queue" << endl;
    for (int el : dd) {
        cout << el << " ";
    }
    cout << endl;
}

void print_vector(vector<int> &dd) {
    cout << "printing vector" << endl;
    for (int el : dd) {
        cout << el << " ";
    }
    cout << endl;
}

int RegretMinimizer::make_decision(vector<vector<int>> &delta_matrix) {
    deque<int> delta_queue = convert_matrix(delta_matrix);

    vector<int> this_worst, others_best, regret;
    for (auto row = delta_matrix.begin(); row != delta_matrix.end(); ++row) {
        this_worst.push_back(*min_element(row->begin(), row->end()));
        for (int i = 0; i < row->size(); ++i) {
            delta_queue.push_back(delta_queue.front());
            delta_queue.pop_front();
        }
        others_best.push_back(*max_element(delta_queue.begin(), delta_queue.begin() + delta_queue.size() - row->size()));
    }
    regret.resize(this_worst.size());
    transform(others_best.begin(), others_best.end(), this_worst.begin(), regret.begin(),minus<>());

    return distance(regret.begin(), min_element(regret.begin(), regret.end()));
}