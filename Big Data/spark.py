from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("project").getOrCreate()

df1 = spark.read.csv("ml-latest-small/movies.csv")
df1.createOrReplaceTempView("Movies")


df2 = spark.read.csv("ml-latest-small/ratings.csv")
df2.createOrReplaceTempView("Ratings")


df = spark.sql("SELECT m._c0 AS id, m._c2 AS genres, r._c2 AS rating FROM Movies AS m, Ratings as r WHERE m._c0 = r._c1")
df.createOrReplaceTempView("Data")

genreList = spark.sql("SELECT DISTINCT genres FROM Data").collect()
parsedList = []
for x in genreList:
    parsedList.append(x["genres"])

genreDist = []
for x in parsedList:
    result = spark.sql("SELECT genres, rating FROM Data WHERE genres = \"" + x + "\"")
    rs = result.groupby("rating").count()
    genreDist.append(rs)

finalNameList = []
finalData = []
index = 0
for x in genreDist:
    x.createOrReplaceTempView("countFrame")
    totalReviews = spark.sql("SELECT SUM(count) as total FROM countFrame").collect()
    finalInt = []
    for y in totalReviews:
        finalInt.append(y["total"])
    if finalInt[0] > 1200:
        print(parsedList[index])
        x.sort("rating").show()
    index += 1

spark.stop()