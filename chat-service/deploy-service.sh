echo "        .__            __              __                 __    "
echo "   ____ |  |__ _____ _/  |_    _______/  |______ ________/  |_  "
echo " _/ ___\|  |  \\__  \\   __\  /  ___/\   __\__  \\_  __ \   __\ "
echo " \  \___|   Y  \/ __ \|  |    \___ \  |  |  / __ \|  | \/|  |   "
echo "  \___  >___|  (____  /__|   /____  > |__| (____  /__|   |__|   "
echo "     \/     \/     \/            \/            \/               "

mvn clean package
docker-compose build
docker-compose up

echo "       .__            __              __                   "
echo "   ____ |  |__ _____ _/  |_    _______/  |_  ____ ______   "
echo " _/ ___\|  |  \\__  \\   __\  /  ___/\   __\/  _ \\____ \  "
echo " \  \___|   Y  \/ __ \|  |    \___ \  |  | (  <_> )  |_> > "
echo "  \___  >___|  (____  /__|   /____  > |__|  \____/|   __/  "
echo "      \/     \/     \/            \/              |__|     "