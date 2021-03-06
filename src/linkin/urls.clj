(ns linkin.urls)


(defn similar-urls
  "Return a set of URLs we think have the same meaning as the supplied URL"
  [^String u]
  (let [u (clojure.string/replace u #"#$|\?$|/$" "")
        opts (map #(str u %) '("#" "/" "/#" "?" "/?"))]
    (set (cons u opts))))


(defn trim-hash-portion
  "Remove the portion of a URL after any #"
  [url]
  (if url
    (first (clojure.string/split url #"#"))
    ""))


(defn already-crawled?
  "Check if we have already crawled this URL or a similar URL (<url> + '#') within the Set of urls"
  [^String url crawled-urls]
  (let [similar-urlset (similar-urls (trim-hash-portion url))]
    (not (empty? (clojure.set/intersection similar-urlset crawled-urls)))))
 

(defn relativize-url
  "Takes in an absolute URL and returns the relative path portion"
  [url]
  (if (.startsWith url "http")
    (str "/" (clojure.string/join "/" (drop 2 (filter (comp not empty?) (clojure.string/split url #"/")))))
    url))


(defn local?
  "Determine if a URL is local relative to a certain base URL"
  [^String url ^String base-url]
  (if (and base-url url) (.startsWith url base-url)))
