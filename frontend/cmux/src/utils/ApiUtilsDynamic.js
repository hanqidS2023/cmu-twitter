import { useContext } from 'react';
import { AuthContext } from '../components/AuthProvider';

export const useFetchWithTokenRefresh = () => {
    const { accessToken, refreshToken, refreshToken: refresh } = useContext(AuthContext);

    const fetchWithRefresh = async (url, options) => {
        const response = await fetch(url, {
            ...options,
            headers: {
                ...options.headers,
                Authorization: `Bearer ${accessToken}`,
            },
        });

        if (response.status === 401 && refreshToken) {
            console.log('Refreshing token...');
            await refresh();
            return fetch(url, {
                ...options,
                headers: {
                    ...options.headers,
                    Authorization: `Bearer ${accessToken}`,
                },
            });
        }

        return response;
    };

    return fetchWithRefresh;
};
