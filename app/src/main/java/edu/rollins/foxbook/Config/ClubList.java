package edu.rollins.foxbook.Config;

import java.util.ArrayList;

public class ClubList {
    private static ClubList _instance;
    public ArrayList<String> m_ClibList;
    public static ClubList getInstance(){
        if(_instance == null){
            _instance = new ClubList();
            _instance.AddClubName();
        }
        return  _instance;
    }

    public void AddClubName(){
        m_ClibList = new ArrayList<>();
        m_ClibList.add("Rollins Collar Scholars");
        m_ClibList.add("Rollins Public Health Association");
        m_ClibList.add("91.5 WPRK");
        m_ClibList.add("Alpha Delta Pi ");
        m_ClibList.add("Alpha Omicron Pi");
        m_ClibList.add("Alpha Psi Omega");
        m_ClibList.add("Amnesty International - Rollins College");
        m_ClibList.add("Art Fest");
        m_ClibList.add("Association of Computing Machinery Student Chapter (ACM)");
        m_ClibList.add("Black Student Union");
        m_ClibList.add("Center for Inclusion and Campus Involvement");
        m_ClibList.add("Classics Club");
        m_ClibList.add("Crummer Entrepreneurship Association");
        m_ClibList.add("Crummer MBA Oath Association");
        m_ClibList.add("Crummer PMBA Association");
        m_ClibList.add("Crummer Rollins Early Advantage MBA Association");
        m_ClibList.add("Crummer Women's Group");
        m_ClibList.add("Dean’s Scholars Council");
        m_ClibList.add("DESI");
        m_ClibList.add("Fiat Flux");
        m_ClibList.add("German Klub");
        m_ClibList.add("Honors Neighborhood");
        m_ClibList.add("Identities: Mirrors and Windows");
        m_ClibList.add("Kappa Delta Sorority");
        m_ClibList.add("Latin American Student Association");
        m_ClibList.add("Muslim Student Union");
        m_ClibList.add("Mysteries and Marvels ");
        m_ClibList.add("Non Compis Mentis");
        m_ClibList.add("Phi Mu Alpha");
        m_ClibList.add("Rollins China Club");
    }

}

