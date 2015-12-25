git config credential.helper store
git commit -m "Adding new deployment"
git push origin master

#ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-77-175-204.eu-west-1.compute.amazonaws.com

ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'sudo git clone https://github.com/JackHopkins/PronunciationInference.git'
ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'cd PronunciationInference/src'
ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'sudo javac *.java'
ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'java -cp . Main'