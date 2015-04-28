(ns website.routes)

(def routes ["/" {"" :index
                  "articles/" {"index.html" :article-index
                               "article.html" :article}}])