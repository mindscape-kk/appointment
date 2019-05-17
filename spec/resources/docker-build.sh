 # clear old builds
 IMAGE_IDS="$(docker images -q  medicom-db)"

 echo "old images will be deleted: ${IMAGE_IDS}"
 docker rmi $IMAGE_IDS


 docker build -t medicom-db ./db