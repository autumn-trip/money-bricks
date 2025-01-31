import BasicMenu from "../../common/pages/BasicMenu";
import OwnProductComponent from "../component/OwnProductComponent";
import FooterComponent from "../../common/component/FooterComponent";

const OwnProductPage = () => {
    return (
        <div>
            <BasicMenu />
            <div>
                <OwnProductComponent />
            </div>
            <FooterComponent />
        </div>
    );
};

export default OwnProductPage;