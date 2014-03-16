#include "wikiClasses.h"


//Standards used by the source html files
const string LINKED_PAGE_IN_HTML = "title=";
const string ARTICLE_NAME_IN_HTML = "wgTitle";


/* Part of the definition of WikiGraph */
WikiGraph::WikiGraph(ifstream& in_file_edges, ifstream& in_file_wiki_info) : Graph(in_file_edges) {
    string ss;
    list<WikiPage> lst;
    int largest_index = 0;
    while (!in_file_wiki_info.eof()) {
        getline(in_file_wiki_info, ss);
        if (ss.length() == 0) break;
        WikiPage new_page;
        new_page.ID = stoi(ss);
        getline(in_file_wiki_info, new_page.title);
        getline(in_file_wiki_info, new_page.html_location);
        getline(in_file_wiki_info, new_page.txt_location);
        lst.push_back(new_page);
        largest_index = (largest_index < new_page.ID) ? new_page.ID : largest_index;
    }
    node_to_wiki.resize(largest_index+1);
    for(auto& x : lst) {
        title_to_node[x.title] = x.ID;
        node_to_wiki[x.ID] = x;
    }
}



/* findAndExtractAssociatedValue : will search the string, but instead of extracting the position where 'field' is found, it actually saves the data associated with this field in 'result' and returns the pointer after this result, for other searches*/
size_t findAndExtractAssociatedValue(const string& inputString, size_t start_pos, const string& field, string&result) {
    auto pos = inputString.find(field, start_pos);
    if( pos != string::npos) {
        auto p1 = inputString.find("\"", pos + field.length());
        auto p2 = inputString.find("\"", p1 + 1);
        result = inputString.substr(p1+1,p2-p1-1);
        return p2+1;
    }else return string::npos;
}

/* getPageTitle : extracts the title of wikipedia page from the HTML source */
string getPageTitle(ifstream& html_file) {
    if(!html_file.is_open()){
        cout << "getPageTitle : the file was not properly opened" << endl;
        return "";
    }
    string result;
    while (!html_file.eof()) {
        string line;
        getline(html_file, line);
        auto pos = findAndExtractAssociatedValue(line, 0, ARTICLE_NAME_IN_HTML, result);
        if(pos != string::npos)
            break;
    }
    return result;
}

/* allAssociations : returns all the title of all pages which are linked (through a hyperlink) in the input file */
set<string> allAssociations(ifstream& f) {
    string field = LINKED_PAGE_IN_HTML;
    set<string> toRet;
    if(!f.is_open()) return toRet;
    while (!f.eof()) {
        string line;
        getline(f, line);
        string result;
        for(auto pos = findAndExtractAssociatedValue(line, 0, field, result);
            pos != string::npos;
            pos = findAndExtractAssociatedValue(line, pos, field, result)) {
            toRet.insert(result);
        }
    }
    return toRet;
}

/* countOccurences : solution to the method in the previous homework */
int countOccurences(ifstream& in_file, string string_to_count) {
    int toRet = 0;
    while (!in_file.eof()) {
        string line;
        getline(in_file, line);
        for (auto pos = line.find(string_to_count);
             pos != string::npos;
             pos = line.find(string_to_count,pos + string_to_count.length())){
            toRet++;
        }
    }
    return toRet;
}
