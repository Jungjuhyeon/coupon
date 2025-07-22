// import http from 'k6/http';
// import { check } from 'k6';  // check 임포트
// import { sleep } from 'k6';
//
// export const options = {
//     // 부하를 생성하는 단계(stages)를 설정
//     stages: [
//         // 10분에 걸쳐 vus(virtual users, 가상 유저수)가 6000에 도달하도록 설정
//         { duration: '5m', target: 4000 }
//     ],
// };
//
// export default function () {
//     // 서버 상태 확인을 위한 GET 요청
//     let res = http.get('http://host.docker.internal:8080/test');  // 응답을 res 변수에 저장
//
//     // 응답 상태 코드가 200인지 체크
//     check(res, {
//         'is status 200': (r) => r.status === 200,
//     });
//     // 1초 휴식
//     sleep(1);
// }

import http from 'k6/http';
import { check } from 'k6';

const eventId = 1;  // 이벤트 ID
const couponId = 1;  // 쿠폰 ID

// export let options = {
//     // vus: 3000,  // 3000명의 가상 유저가 동시 실행
//     // duration: '5m',  // 5초 동안 실행
//     duration: '5m',
//     target: 1000
// };
export let options = {
    stages: [
        { duration: '10m', target: 5000 },  // 5분 동안 1000명의 유저가 증가
    ],
};
export default function () {
    // __VU는 1부터 시작하는 각 VU의 인덱스를 제공합니다.
    let memberId = __VU;  // 1 ~ 3000 사이의 유니크한 memberId 생성

    let url = `http://host.docker.internal:8080/api/v1/event/${eventId}/issue`;

    // 요청 파라미터로 memberId, couponId를 전송
    let params = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    };

    // POST 요청 보내기
    let res = http.post(url, `couponId=${couponId}&memberId=${memberId}`, params);

    // 응답 상태 코드가 200인지 체크
    check(res, {
        'is status 200': (r) => r.status === 200,
    });
}
