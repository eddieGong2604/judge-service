package com.csit314.testservice.service.constants;

public class SourceCodeConstants {
    public static final String CORRECT_SOURCE_CODE = "#include <iostream>\n" +
            "#include <string>\n" +
            "#include <climits>\n" +
            "#include <math.h>\n" +
            "#include <float.h>\n" +
            "\n" +
            "using namespace std;\n" +
            "char fileName[50];\n" +
            "int nVertices, nEdges;\n" +
            "struct Vertex {\n" +
            "    double x;\n" +
            "    double y;\n" +
            "};\n" +
            "struct Dist {\n" +
            "    int vertex;\n" +
            "    double distance;\n" +
            "};\n" +
            "Vertex *vertices;\n" +
            "double **edges;\n" +
            "int start, goal;\n" +
            "\n" +
            "double findShortestPath(Vertex *vertices, double **edges, int nVertices, int start, int goal, int *path);\n" +
            "\n" +
            "void siftup(Dist *heap, int pos);\n" +
            "\n" +
            "void swap(Dist *heap, int pos1, int pos2);\n" +
            "\n" +
            "void makeheap(Dist *heap, int end);\n" +
            "\n" +
            "void siftdown(Dist *heap, int end, int pos);\n" +
            "\n" +
            "double heuristic(int vertex, int goal);\n" +
            "\n" +
            "double findSecondShortestPath(Vertex *vertices, double **edges, int nVertices, int start, int goal, int *shortestPath,\n" +
            "                              int *secondShortestPath);\n" +
            "\n" +
            "int main() {\n" +
            "    cin >> nVertices >> nEdges;\n" +
            "    if (nVertices < 0 || nEdges < 0) {\n" +
            "        cout << \"Invalid input.\" << endl;\n" +
            "        return 0;\n" +
            "    }\n" +
            "    vertices = new Vertex[nVertices + 1];\n" +
            "    edges = new double *[nVertices + 1];\n" +
            "    int *path = new int[nVertices + 1];\n" +
            "    int *path2nd = new int[nVertices + 1];\n" +
            "    for (int i = 0; i <= nVertices; i++) {\n" +
            "        edges[i] = new double[nVertices + 1];\n" +
            "        for (int j = 0; j <= nVertices; j++) {\n" +
            "            edges[i][j] = INT_MAX;\n" +
            "        }\n" +
            "    }\n" +
            "    int label;\n" +
            "    double x, y;\n" +
            "    for (int i = 0; i < nVertices; i++) {\n" +
            "        Vertex vertex;\n" +
            "        cin >> label >> x >> y;\n" +
            "        if (label < 0) {\n" +
            "            cout << \"Invalid input.\" << endl;\n" +
            "            return 0;\n" +
            "        }\n" +
            "        vertices[label].x = x;\n" +
            "        vertices[label].y = y;\n" +
            "    }\n" +
            "\n" +
            "    int edge1, edge2;\n" +
            "    double weight;\n" +
            "    for (int i = 0; i < nEdges; i++) {\n" +
            "        cin >> edge1 >> edge2 >> weight;\n" +
            "        if (edge1 < 0 || edge2 < 0 || weight < 0) {\n" +
            "            cout << \"Invalid input.\" << endl;\n" +
            "            return 0;\n" +
            "        }\n" +
            "        if (edges[edge1][edge2] == DBL_MAX || edges[edge1][edge2] > weight) {\n" +
            "            edges[edge1][edge2] = weight;\n" +
            "            edges[edge2][edge1] = weight;\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "    cin >> start >> goal;\n" +
            "    if (start < 0 || goal < 0) {\n" +
            "        cout << \"Invalid input.\" << endl;\n" +
            "        return 0;\n" +
            "    }\n" +
            "    double shortestPath = findShortestPath(vertices, edges, nVertices, start, goal, path);\n" +
            "    if (shortestPath == DBL_MAX) {\n" +
            "        cout << \"There is no path from \" << start << \" to \" << goal << \".\" << endl;\n" +
            "        return 0;\n" +
            "    }\n" +
            "    cout << \"Shortest path from \" << start << \" to \" << goal << \": \" << endl;\n" +
            "    cout << \"Vertices of path: \";\n" +
            "    int v = start;\n" +
            "    while (v != goal) {\n" +
            "        cout << v << \"-->\";\n" +
            "        v = path[v];\n" +
            "    };\n" +
            "    cout << goal << endl;\n" +
            "    cout << \"Length of path: \" << shortestPath << endl;\n" +
            "    cout << endl;\n" +
            "    double secondShortestPath = findSecondShortestPath(vertices, edges, nVertices, start, goal, path, path2nd);\n" +
            "    if (secondShortestPath == DBL_MAX) {\n" +
            "        cout << \"There is no second shortest path from \" << start << \" to \" << goal << \".\" << endl;\n" +
            "        return 0;\n" +
            "    }\n" +
            "    cout << \"Second shortest path from \" << start << \" to \" << goal << \": \" << endl;\n" +
            "    cout << \"Vertices of path: \";\n" +
            "    v = start;\n" +
            "    while (v != goal) {\n" +
            "        cout << v << \"-->\";\n" +
            "        v = path2nd[v];\n" +
            "    };\n" +
            "    cout << goal << endl;\n" +
            "    cout << \"Length of path: \" << secondShortestPath << endl;\n" +
            "    return 0;\n" +
            "}\n" +
            "\n" +
            "double findShortestPath(Vertex *vertices, double **edges, int nVertices, int start, int goal, int *path) {\n" +
            "    Dist *distance = new Dist[nVertices];\n" +
            "    int endDistance = 0;\n" +
            "    bool ifShortestPathExists = false;\n" +
            "    double size[nVertices + 1];\n" +
            "    for (int i = 0; i < nVertices + 1; i++) {\n" +
            "        size[i] = DBL_MAX;\n" +
            "    }\n" +
            "    for (int i = 1; i <= nVertices; i++) {\n" +
            "        if (i != goal) {\n" +
            "            if (edges[goal][i] != DBL_MAX) {\n" +
            "                path[i] = goal;\n" +
            "            }\n" +
            "            size[i] = edges[goal][i];\n" +
            "            if (i == start) {\n" +
            "                ifShortestPathExists = true;\n" +
            "            }\n" +
            "            distance[endDistance].vertex = i;\n" +
            "            distance[endDistance].distance = edges[goal][i];\n" +
            "            endDistance++;\n" +
            "            siftup(distance, endDistance - 1);\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "    do {\n" +
            "        Dist v = distance[0];\n" +
            "        if (v.vertex == start && ifShortestPathExists && size[start] < 1000000) {\n" +
            "            return size[start];\n" +
            "        }\n" +
            "        endDistance--;\n" +
            "        distance[0] = distance[endDistance];\n" +
            "        for (int i = 0; i < endDistance; i++) {\n" +
            "            Dist u = distance[i];\n" +
            "            if (edges[v.vertex][u.vertex] != INT_MAX && u.distance > v.distance + edges[v.vertex][u.vertex]) {\n" +
            "                distance[i].distance = v.distance + edges[v.vertex][u.vertex];\n" +
            "                path[u.vertex] = v.vertex;\n" +
            "                size[u.vertex] = v.distance + edges[v.vertex][u.vertex]; //update the total path\n" +
            "                if (u.vertex == start) {\n" +
            "                    ifShortestPathExists = true;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        makeheap(distance, endDistance);\n" +
            "    } while (endDistance > 0);\n" +
            "    if (ifShortestPathExists && size[start] < 1000000) {\n" +
            "        return size[start];\n" +
            "    } else {\n" +
            "        return DBL_MAX;\n" +
            "    }\n" +
            "};\n" +
            "\n" +
            "\n" +
            "double findSecondShortestPath(Vertex *vertices, double **edges, int nVertices, int start, int goal, int *shortestPath,\n" +
            "                              int *secondShortestPath) {\n" +
            "    int v = start;\n" +
            "    double secondShortestPathSize = DBL_MAX;\n" +
            "    bool ifSecondShortestPathExists = false;\n" +
            "    while (v != goal) {\n" +
            "        int *path = new int[nVertices + 1];\n" +
            "        int next = shortestPath[v];\n" +
            "        double removedEdge = edges[v][next];\n" +
            "        edges[v][next] = DBL_MAX;\n" +
            "        edges[next][v] = DBL_MAX;\n" +
            "        double pathSize = findShortestPath(vertices, edges, nVertices, start, goal,\n" +
            "                                           path); //find shortest path not counting edge e[i]\n" +
            "        edges[v][next] = removedEdge;\n" +
            "        edges[next][v] = removedEdge;\n" +
            "        if (pathSize < secondShortestPathSize) {\n" +
            "            ifSecondShortestPathExists = true;\n" +
            "            secondShortestPathSize = pathSize;\n" +
            "            for (int i = 0; i <= nVertices; i++) {\n" +
            "                secondShortestPath[i] = path[i];\n" +
            "            }\n" +
            "        };\n" +
            "        v = next;\n" +
            "    }\n" +
            "    if (ifSecondShortestPathExists) {\n" +
            "        return secondShortestPathSize;\n" +
            "    } else {\n" +
            "        return DBL_MAX;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "void makeheap(Dist *heap, int end) {\n" +
            "    for (int i = end / 2; i >= 0; i--) {\n" +
            "        siftdown(heap, end, i);\n" +
            "    };\n" +
            "};\n" +
            "\n" +
            "void siftdown(Dist *heap, int end, int pos) {\n" +
            "    int child = pos * 2 + 1; //since my heap start from 0 => child = pos*2+1 and pos*2+2\n" +
            "    if (pos == end - 1 || child > end - 1) //if pos reached end of array=> return\n" +
            "        return;\n" +
            "    if (child + 1 < end && heap[child].distance + heuristic(heap[child].vertex, start) >\n" +
            "                           heap[child + 1].distance + heuristic(heap[child + 1].vertex, start)) {\n" +
            "        child++;\n" +
            "    };\n" +
            "    if (heap[pos].distance + heuristic(heap[pos].vertex, start) >\n" +
            "        heap[child].distance + heuristic(heap[child].vertex, start)) {\n" +
            "        swap(heap, pos, child);\n" +
            "        siftdown(heap, end, child);\n" +
            "    };\n" +
            "};\n" +
            "\n" +
            "void siftup(Dist *heap, int pos) {\n" +
            "    if (pos == 0) {\n" +
            "        return;\n" +
            "    }\n" +
            "    int parent = (pos - 1) / 2;\n" +
            "    if (heap[parent].distance + heuristic(heap[parent].vertex, start) <\n" +
            "        heap[pos].distance + heuristic(heap[pos].vertex, start)) {\n" +
            "        return;\n" +
            "    } else {\n" +
            "        swap(heap, parent, pos);\n" +
            "        siftup(heap, parent);\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "void swap(Dist *heap, int pos1, int pos2) {\n" +
            "    Dist temp = heap[pos1];\n" +
            "    heap[pos1] = heap[pos2];\n" +
            "    heap[pos2] = temp;\n" +
            "};\n" +
            "\n" +
            "double heuristic(int vertex, int start) {\n" +
            "    return sqrt(pow(vertices[vertex].x - vertices[start].x, 2) + pow(vertices[vertex].y - vertices[start].y, 2));\n" +
            "}\n";
}
