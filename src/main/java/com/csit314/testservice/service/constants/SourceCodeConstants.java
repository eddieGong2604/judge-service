package com.csit314.testservice.service.constants;

public class SourceCodeConstants {
    public static final String CORRECT_SOURCE_CODE = "#include <iostream>\n" +
            "#include <fstream>\n" +
            "#include <string>\n" +
            "#include <climits>\n" +
            "#include <math.h>\n" +
            "#include <cfloat>\n" +
            "\n" +
            "using namespace std;\n" +
            "char fileName[50];\n" +
            "int nVertices, nEdges;\n" +
            "\n" +
            "struct Vertex {\n" +
            "    double x;\n" +
            "    double y;\n" +
            "};\n" +
            "\n" +
            "struct Dist {\n" +
            "    int vertex;\n" +
            "    double distance;\n" +
            "};\n" +
            "\n" +
            "\n" +
            "Vertex *vertices;\n" +
            "double **edges;\n" +
            "int start, goal;\n" +
            "\n" +
            "double findShortestPath (Vertex *vertices, double **edges, int nVertices, int start, int goal, int *path);\n" +
            "void siftup(Dist *heap, int pos);\n" +
            "void swap(Dist *heap, int pos1, int pos2);\n" +
            "void makeheap(Dist *heap, int end);\n" +
            "void siftdown(Dist *heap, int end, int pos);\n" +
            "double heuristic(int vertex, int goal);\n" +
            "double findSecondShortestPath (Vertex *vertices, double **edges, int nVertices, int start, int goal, int *shortestPath, int *secondShortestPath);\n" +
            "\n" +
            "int main () {\n" +
            "    \n" +
            "    cin>>nVertices>>nEdges;\n" +
            "    vertices = new Vertex[nVertices+1];\n" +
            "    edges = new double*[nVertices+1];\n" +
            "    int *path = new int[nVertices+1];\n" +
            "    int *path2nd = new int[nVertices+1];\n" +
            "    for (int i = 0; i <= nVertices ; i++) {\n" +
            "        edges[i] = new double[nVertices+1];\n" +
            "        for (int j = 0; j <= nVertices ; j++) {\n" +
            "            edges[i][j] = INT_MAX;\n" +
            "        }\n" +
            "    }\n" +
            "    int label;\n" +
            "    double x, y;\n" +
            "    for (int i = 0; i< nVertices; i++) {\n" +
            "         Vertex vertex;\n" +
            "        cin >> label >> x >> y;\n" +
            "        vertices[label].x = x;\n" +
            "        vertices[label].y = y;\n" +
            "    }\n" +
            "\n" +
            "    int edge1, edge2;\n" +
            "    double weight;\n" +
            "    for (int i = 0; i < nEdges ; i++) {\n" +
            "            cin >> edge1 >> edge2 >> weight;\n" +
            "            if (edges[edge1][edge2] == DBL_MAX || edges[edge1][edge2] > weight) {\n" +
            "                edges[edge1][edge2] = weight;\n" +
            "                edges[edge2][edge1] = weight;\n" +
            "            }\n" +
            "        \n" +
            "    }\n" +
            "    cin >> start >> goal;\n" +
            "\n" +
            "    if(start == goal){\n" +
            "        cout <<\"Shortest path from \"<< start <<\" to \" << goal <<\": \" << endl;\n" +
            "        cout << \"Vertices of path: \" << start << \"-->\"<< goal << endl;\n" +
            "        cout << \"Length of path: \" << 0 << endl;\n" +
            "        cout << \"There is no second shortest path.\" << endl\n" +
            "        return;\n" +
            "    }\n" +
            "\n" +
            "    int length;\n" +
            "    double shortestPath = findShortestPath (vertices, edges, nVertices, start, goal, path);\n" +
            "\n" +
            "    cout <<\"Shortest path from \"<< start <<\" to \" << goal <<\": \" << endl;\n" +
            "    cout << \"Vertices of path: \";\n" +
            "    int v = start;\n" +
            "    while (v!=goal) {\n" +
            "        cout << v <<\"-->\";\n" +
            "        v = path[v];\n" +
            "    };\n" +
            "    cout << goal << endl;\n" +
            "    cout << \"Length of path: \" << shortestPath << endl;\n" +
            "\n" +
            "    double secondShortestPath = findSecondShortestPath (vertices, edges, nVertices, start, goal, path, path2nd);\n" +
            "    cout <<\"\\nSecond shortest path from \"<< start <<\" to \" << goal <<\": \" << endl;\n" +
            "    cout<<\"Vertices of path: \";\n" +
            "    v = start;\n" +
            "    while (v!=goal) {\n" +
            "        cout << v <<\"-->\";\n" +
            "        v = path2nd[v];\n" +
            "    };\n" +
            "    cout << goal << endl;\n" +
            "    cout << \"Length of path: \"<< secondShortestPath << endl;\n" +
            "\n" +
            "    return 0;\n" +
            "}\n" +
            "\n" +
            "double findShortestPath (Vertex *vertices, double **edges, int nVertices, int start, int goal, int *path) {\n" +
            "    Dist *distance = new Dist[nVertices]; \n" +
            "    int endDistance = 0;\n" +
            "    double size[nVertices+1]; \n" +
            "    for (int i = 1; i <=nVertices; i++ ) {\n" +
            "        if (i != goal) {\n" +
            "            if (edges[goal][i]!=DBL_MAX ) {\n" +
            "                path[i] = goal;\n" +
            "            }\n" +
            "            size[i] = edges[goal][i];\n" +
            "            distance[endDistance].vertex = i;\n" +
            "            distance[endDistance].distance = edges[goal][i];\n" +
            "            endDistance++;\n" +
            "            siftup(distance, endDistance-1);\n" +
            "        }\n" +
            "        \n" +
            "    }\n" +
            "    \n" +
            "    do {\n" +
            "        Dist v = distance[0]; \n" +
            "        if (v.vertex == start) {\n" +
            "            return size[start];\n" +
            "        }\n" +
            "\n" +
            "        endDistance--;\n" +
            "        distance[0] = distance[endDistance];\n" +
            "        for (int i = 0; i < endDistance ; i++) {\n" +
            "            Dist u = distance[i];\n" +
            "            if ( edges[v.vertex][u.vertex] != INT_MAX  && u.distance > v.distance + edges[v.vertex][u.vertex] ) {\n" +
            "                distance[i].distance = v.distance + edges[v.vertex][u.vertex];\n" +
            "                path[u.vertex] = v.vertex;\n" +
            "                size[u.vertex] = v.distance + edges[v.vertex][u.vertex]; //update the total path\n" +
            "            }\n" +
            "        }\n" +
            "        makeheap(distance, endDistance);\n" +
            "    }\n" +
            "    while (endDistance > 0);\n" +
            "\n" +
            "    return size[start];\n" +
            "};\n" +
            "\n" +
            "\n" +
            "double findSecondShortestPath (Vertex *vertices, double **edges, int nVertices, int start, int goal, int *shortestPath, int *secondShortestPath) {\n" +
            "    int v = start;\n" +
            "    double secondShortestPathSize = DBL_MAX;\n" +
            "    while (v!=goal) {\n" +
            "        int *path = new int[nVertices+1];\n" +
            "        int next = shortestPath[v];\n" +
            "        double removedEdge = edges[v][next];\n" +
            "        edges[v][next] = DBL_MAX;\n" +
            "        edges[next][v] = DBL_MAX;\n" +
            "        double pathSize = findShortestPath(vertices, edges, nVertices, start, goal, path); //find shortest path not counting edge e[i]\n" +
            "        edges[v][next] = removedEdge;\n" +
            "        edges[next][v] = removedEdge;\n" +
            "        if (pathSize < secondShortestPathSize) {\n" +
            "            secondShortestPathSize = pathSize;\n" +
            "            for (int i = 0; i<= nVertices; i++) {\n" +
            "                secondShortestPath[i] = path[i];\n" +
            "            }\n" +
            "        };\n" +
            "        v = next;\n" +
            "    }\n" +
            "\n" +
            "    return secondShortestPathSize;\n" +
            "}\n" +
            "\n" +
            "void makeheap(Dist *heap, int end) {\n" +
            "    for(int i=end/2; i>=0;i--) {\n" +
            "        siftdown(heap, end, i);\n" +
            "    };\n" +
            "};\n" +
            "\n" +
            "void siftdown(Dist *heap, int end, int pos) {\n" +
            "    int child = pos*2+1; //since my heap start from 0 => child = pos*2+1 and pos*2+2\n" +
            "    if(pos==end-1|| child>end-1) //if pos reached end of array=> return \n" +
            "        return;\n" +
            "    if(child+1<end && heap[child].distance + heuristic(heap[child].vertex, start) > heap[child+1].distance + heuristic(heap[child+1].vertex, start)) {\n" +
            "        child++;\n" +
            "    };\n" +
            "    if(heap[pos].distance + heuristic(heap[pos].vertex, start) > heap[child].distance + heuristic(heap[child].vertex, start)) {\n" +
            "        swap(heap, pos,child);\n" +
            "        siftdown(heap,end,child);\n" +
            "    };\n" +
            "};\n" +
            "\n" +
            "void siftup(Dist *heap, int pos) {\n" +
            "    if(pos==0) {\n" +
            "        return;\n" +
            "    }\n" +
            "    int parent=(pos-1)/2;\n" +
            "    if (heap[parent].distance + heuristic(heap[parent].vertex, start) <heap[pos].distance + heuristic(heap[pos].vertex, start)) {\n" +
            "        return;\n" +
            "    } else {\n" +
            "        swap(heap, parent, pos);\n" +
            "        siftup(heap, parent);\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "void swap(Dist *heap, int pos1, int pos2) {\n" +
            "    Dist temp=heap[pos1];\n" +
            "    heap[pos1]=heap[pos2];\n" +
            "    heap[pos2]=temp;\n" +
            "};\n" +
            "\n" +
            "double heuristic(int vertex, int start) {\n" +
            "    return sqrt(pow(vertices[vertex].x-vertices[start].x, 2)+pow(vertices[vertex].y-vertices[start].y,2));\n" +
            "}\n";
}
