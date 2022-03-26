import React, { useState } from 'react';
import DaumPostcode from 'react-daum-postcode';

const DaumPost = ({returnFullAddress}) => {

    const handleComplete = (data) => {
        let fullAddress = data.address;
        let extraAddress = '';
        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName);
            }
            fullAddress += (extraAddress !== '' ? ` (${extraAddress})` : '');
        }
        returnFullAddress(fullAddress);
    }

    return (
        <DaumPostcode onComplete={handleComplete} className="post-code" />
    );
};

export default DaumPost;