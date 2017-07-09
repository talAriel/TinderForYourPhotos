csv_file =read.csv("data.csv",header=T,na.strings="?")

cols = c("url", "objectsh","finalrank","facerank","strongcol1percent")
new_data = subset(csv_file, select=cols)
write.csv(new_data,file = "photos.csv",row.names=FALSE)
