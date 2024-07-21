#Generate an RSA private key of size 2048:
openssl genrsa -des3 -out rootCA.key 2048

#Generate a root certificate valid for two years:
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 730 -out rootCert.pem

#Generate a private key for your certificate:
openssl genrsa -out cert.key 2048

#Use the private key to create a certificate signing request (CSR):
openssl req -new -key cert.key -out cert.csr

vim openssl.cnf
 # Extensions to add to a certificate request
 #basicConstraints       = CA:FALSE
 #authorityKeyIdentifier = keyid:always, issuer:always
 #keyUsage               = nonRepudiation, digitalSignature, keyEncipherment, dataEncipherment
 #subjectAltName         = @alt_names
 #[ alt_names ]
 #DNS.1 = *.mra.io
 
#Sign the CSR using the root certificate and key:
openssl x509 -req -in cert.csr -CA rootCert.pem -CAkey rootCA.key -CAcreateserial -out cert.crt -days 730 -sha256 -extfile openssl.cnf

#Verify that the certificate is built correctly:
openssl verify -CAfile rootCert.pem -verify_hostname local.yourhostname cert.crt

To avoid browser warnings about untrusted certificates, add the root certificate as a trusted certificate in Chrome by following these steps:

#Open Chrome settings, and select Security > Manage Certificates.
#Click the Authorities tab, then click the Import… button. This opens the Certificate Import Wizard.
#Click Next to get to the File to the Import screen.
#Click Browse… and select rootCert.pem then click Next.
#Check Trust this certificate for identifying websites then click OK to finish the process.

#genrate base64 contents
base64 -i cert.key | tr -d '\n' > tls.key.base64
base64 -i cert.crt | tr -d '\n' > tls.crt.base64

#Credits: https://medium.com/@eng.fadishaar/step-by-step-guide-configuring-nginx-with-https-on-localhost-for-secure-web-application-testing-c78febc26c78