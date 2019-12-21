
#ifndef CPP_PLAYER_H
#define CPP_PLAYER_H

#include <deque>
#include "number_match.h"

const string MAXIMIZE_GAIN = "Maximize-Gain";
const string MINIMIZE_LOSS = "Minimize-Loss";
const string MINIMIZE_REGRET = "Minimize-Regret";

class Player {
public:
    virtual int get_size() = 0;
    virtual void update_number(int, int) = 0;
    virtual multiset<int> &get_numbers() = 0;
    virtual int make_decision(vector<vector<int>> &) = 0;
    virtual ~Player() = default;
};


class GainMaximizer : public Player {
private:
    static string type;
    multiset<int> numbers;

public:
    GainMaximizer(multiset<int> &numbers) : numbers(numbers) { }
    int get_size() override;
    void update_number(int, int) override;
    multiset<int> &get_numbers() override;
    int make_decision(vector<vector<int>> &) override;
    ~GainMaximizer() override = default;
};


class LossMinimizer : public Player {
private:
    static string type;
    multiset<int> numbers;

public:
    LossMinimizer(multiset<int> &numbers) : numbers(numbers) {}
    int get_size() override;
    void update_number(int, int) override;
    multiset<int> &get_numbers() override;
    int make_decision(vector<vector<int>> &) override;
    ~LossMinimizer() override = default;
};

class RegretMinimizer : public Player {
private:
    static string type;
    multiset<int> numbers;

public:
    RegretMinimizer(multiset<int> &numbers) : numbers(numbers) {}
    int get_size() override;
    void update_number(int, int) override;
    multiset<int> &get_numbers() override;
    int make_decision(vector<vector<int>> &) override;
    ~RegretMinimizer() override = default;
};

#endif //CPP_PLAYER_H
