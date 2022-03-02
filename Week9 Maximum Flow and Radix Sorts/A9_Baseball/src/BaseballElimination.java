/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php
Author: Yu Chen
*/

import edu.princeton.cs.algs4.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BaseballElimination {
    private final int N;      // team numbers (1st line in the txt file)
    private final HashMap<String, Integer> teamMap;
    private final String[] teamName;
    private final int[] w;     // array for wins
    private final int[] l;     // array for losses
    private final int[] r;     // array for remained games
    private final int[][] g;
    private int maxWin;
    private int maxWinTeamID;
    private List<String> listElimination;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException("filename is null");
        // read in file data
        In in = new In(filename);
        N = Integer.parseInt(in.readLine());
        teamMap = new HashMap<>();
        teamName = new String[N];
        w = new int[N];
        l = new int[N];
        r = new int[N];
        g = new int[N][N];

        int i = 0;   // team id
        maxWin = 0;
        while (!in.isEmpty()) {
            String name = in.readString();
            teamMap.put(name, i);
            teamName[i] = name;
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < N; j++) {
                g[i][j] = in.readInt();
            }
            if (w[i] > maxWin) {
                maxWin = w[i];
                maxWinTeamID = i;
            }

            i++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        return teamMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) throw new IllegalArgumentException("team is null");
        return w[teamMap.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) throw new IllegalArgumentException("team is null");
        return l[teamMap.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) throw new IllegalArgumentException("team is null");
        return r[teamMap.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null) throw new IllegalArgumentException("team1 is null");
        if (team2 == null) throw new IllegalArgumentException("team2 is null");
        return g[teamMap.get(team1)][teamMap.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) throw new IllegalArgumentException("team is null");

        if (trivalElimination(team)) {
            return true;
        } else {
            return nontrivalElimination(team);
        }
    }

    private boolean trivalElimination(String team) {
        return (w[teamMap.get(team)] + r[teamMap.get(team)] < maxWin);
    }

    private boolean nontrivalElimination(String team) {
        int teamID = teamMap.get(team);
        int size = N * (N - 1) / 2;  // size for game vertices

        // construct flow network
        FlowNetwork fn = new FlowNetwork(size + 2);
        int s = size;
        int t = size + 1;

        int gameId = N - 1;  // first N is reserved for the edge to t
        int team1, team2, totalRemaining = 0;
        for (int i = 0; i < N; i++) {
            if (i == teamID) {
                continue;
            } else if (i > teamID) {
                team1 = i - 1; // as teamId is skipped, so greater than teamId will get 1 less
            } else {
                team1 = i;
            }
            for (int j = i + 1; j < N; j++) {
                if (j == teamID) {
                    continue;
                } else if (j > teamID) {
                    team2 = j - 1;
                } else {
                    team2 = j;
                }

                // add edges from source s to flow network
                fn.addEdge(new FlowEdge(s, gameId, g[i][j]));
                totalRemaining += g[i][j];

                // add edges from game verteces to team verteces
                fn.addEdge(new FlowEdge(gameId, team1, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(gameId++, team2, Double.POSITIVE_INFINITY));
            }
        }
        // add edges from team verteces to sink t
        for (int i = 0; i < N; i++) {
            if (i == teamID) {
                continue;
            } else if (i > teamID) {
                team1 = i - 1;
            } else {
                team1 = i;
            }
            int capacity = w[teamID] + r[teamID] - w[i];
            if (capacity < 0) {
                continue;
            } else {
                fn.addEdge(new FlowEdge(team1, t, capacity));
            }
        }

        // run Ford-Fulkerson algorithm
        FordFulkerson ff = new FordFulkerson(fn, s, t);
        listElimination = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            if (i == teamID) {
                continue;
            } else if (i > teamID) {
                team1 = i - 1;
            } else {
                team1 = i;
            }
            if (ff.inCut(team1)) {
                listElimination.add(teamName[i]);
            }
        }

        return (int) ff.value() != totalRemaining;
    }


    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) throw new IllegalArgumentException("team is null");

        if (!isEliminated(team)) {
            return null;
        }
        if (trivalElimination(team)) {
            listElimination = new LinkedList<>();
            listElimination.add(team);
        } else {
            nontrivalElimination(team);
        }

        return listElimination;
    }

    // output
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}


