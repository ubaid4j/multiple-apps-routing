### Prerequisite
1. minikube
2. ingress addon
3. Add following entries in /etc/hosts file
   ```
    192.168.49.2 red.mra.io
    192.168.49.2 blue.mra.io
    192.168.49.2 green.mra.io
    192.168.49.2 auth.mra.io
    192.168.49.2 mra.io
   ```
4. Create namespace: `kubectl create namespace mra`
5. Create TLS secret: `kubectl apply -f tls`
6. Restart ingress-nginx-controller: `kubectl rollout restart deployment ingress-nginx-controller -n ingress-ngin`
   1. Check tls related logs: `kubectl logs -n ingress-nginx deployment/ingress-nginx-controller | grep -i tls`
7. Deploy redis: `kubectl apply -f redis`
   1. We can connect redis by using (minikube ip (192.168.49.2) and port 32600)
8. Deploy keycloak: `kubectl apply -f keycloak`

### apps deployment
1. Deploy api-gateway: `kubectl apply -f api-gateway`
2. Deploy red-app: `kubectl apply -f red-app`

