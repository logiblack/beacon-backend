#!/usr/bin/env bash

checkEnv(){
    hash docker 2>/dev/null||{echo >&2 "docker not installed."}
    hash java 2>/dev/null||{echo >&2 "java environment not installed."}
    hash docker-compose 2>/dev/null||{echo >&2 "docker-compose environment not installed."}
}

genWxConfig(){
    if [[ ! -d src/main/resources/config ]]; then
        mkdir src/main/resources/config
    fi

    if [[ ! -f src/main/resources/config/wechat.properties  ]];then
        touch src/main/resources/config/wechat.properties;
        cat > src/main/resources/config/wechat.properties <<EOF
app_id=${app_id}
auth_url=https://api.weixin.qq.com/sns/jscode2session?appid={?}&secret={?}&js_code={?}&grant_type=authorization_code
app_secret=${app_secret}
EOF
    fi
}
buildProject(){
    bash gradlew clean bootJar;
    
    if [[ -f dist/beacon/beacon-latest.jar  ]];then
        rm -f dist/beacon/beacon-latest.jar;
    fi

    if [[ -f "dist/beacon/application-prod.yaml" ]]; then
        rm -f dist/beacon/application-prod.yaml;
    fi
    
    cp build/libs/beacon-*jar dist/beacon/beacon-latest.jar;
    cp src/main/resources/application-docker.yaml dist/beacon/application-prod.yaml;
    docker-compose down;
    docker-compose build --no-cache;
    docker-compose up -d;
}

checkEnv;
genWxConfig;
buildProject;
