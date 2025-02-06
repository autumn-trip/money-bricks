import {Suspense, lazy} from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";

const LoginPage = lazy(() => import("../../common/login/pages/LoginPage"));
const LogoutPage = lazy(() => import("../../common/login/pages/LogoutPage"));
const KakaoRedirect = lazy(() => import("../../common/login/pages/KakaoRedirectPage"));

const loginRouter = () => {
    return [
        {
            path: "login",
            element: (
                <Suspense fallback={<LoadingSpinner isLoading={true}/>}> {/* 로딩 스피너 적용 */}
                    <LoginPage/>
                </Suspense>
            ),
        },
        {
            path: "logout",
            element: (
                <Suspense fallback={<LoadingSpinner isLoading={true}/>}>
                    <LogoutPage/>
                </Suspense>
            ),
        },
        {
            path: "kakao",
            element: (
                <Suspense fallback={<LoadingSpinner isLoading={true}/>}>
                    <KakaoRedirect/>
                </Suspense>
            ),
        },
    ];
};

export default loginRouter;