git config credential.helper store

git commit -m "Adding new deployment" .
git push origin master

ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'sudo rm -rf PronunciationInference'
ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'sudo git clone https://github.com/JackHopkins/PronunciationInference.git'
ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'cd PronunciationInference/PronunciationInference/'
ssh -i /Users/jack/Downloads/mac-key.pem ubuntu@ec2-54-229-5-172.eu-west-1.compute.amazonaws.com 'sudo java -jar ./PronunciationInference.jar'