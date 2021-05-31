#create function
import pandas as pd
from os.path import dirname,join
from sklearn.feature_extraction.text import TfidfVectorizer
import numpy as np
import re
from sklearn.metrics.pairwise import cosine_similarity

def matFn(location):
    tfidf = TfidfVectorizer(max_features=10000,ngram_range = (1, 2),min_df=2)
    filename = join(dirname(__file__),"remake_darktour.csv")# csv 읽어오기
    return_tfidf = pd.read_csv(filename)
    explain = return_tfidf['explain']
    tfidf_matrix = tfidf.fit_transform(explain)


    cosine_matrix = cosine_similarity(tfidf_matrix, tfidf_matrix)
    
    # movie title와 id를 매핑할 dictionary를 생성해줍니다. 
    movie2id = {}
    for i, c in enumerate(return_tfidf['name']): movie2id[i] = c

    # id와 movie title를 매핑할 dictionary를 생성해줍니다. 
    id2movie = {}
    for i, c in movie2id.items(): id2movie[c] = i
    # movie title와 id를 매핑할 dictionary를 생성해줍니다. 
    addressid = {}
    for i, c in enumerate(return_tfidf['address']): addressid[i] = c[0:2]

    # id와 movie title를 매핑할 dictionary를 생성해줍니다. 
    id2address = {}
    for i, c in addressid.items(): 
        id2address[c[0:2]] = i
    
    idx = id2movie['부산민주공원'] # Toy Story : 0번 인덱스 
    sim_scores = [(i, c) for i, c in enumerate(cosine_matrix[idx]) if i != idx] # 자기 자신을 제외한 영화들의 유사도 및 인덱스를 추출 
    sim_scores = sorted(sim_scores, key = lambda x: x[1], reverse=True) # 유사도가 높은 순서대로 정렬 
    address_scores = [(addressid[i], score,movie2id[i]) for i, score in sim_scores[0:]]

    
    location_finish = []
    for x in address_scores[0:]:
        if x[0] == location:
            location_finish.append(x[1:3])

    return location_finish

