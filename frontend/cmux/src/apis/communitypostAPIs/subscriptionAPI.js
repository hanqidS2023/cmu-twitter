import { useFetchWithTokenRefresh } from "../../utils/ApiUtilsDynamic";
export const useSubscriptionApi = () => {
    const fetchWithTokenRefresh = useFetchWithTokenRefresh();

    const fetchUser = async (user_url, query) => {
        try {
            const response = await fetchWithTokenRefresh(`${user_url}?${query}`, { method: 'GET' });
            const data = await response.json();
            return data;
        } catch (error) {
            console.error("Error during search user:", error);
            return [];
        }
    };

    const fetchFollowers = async (user_url, query) => {
        try {
            const response = await fetchWithTokenRefresh(`${user_url}?${query}`, { method: 'GET' });
            const data = await response.json();
            return data;
        } catch (error) {
            console.error("Error during search:", error);
            return 0;
        }
    };

    const fetchSubscriptions = async (user_url, query) => {
        try {
            const response = await fetchWithTokenRefresh(`${user_url}?${query}`, { method: 'GET' });
            const data = await response.json();
            return data;
        } catch (error) {
            console.error("Error during search:", error);
            return 0;
        }
    };

    const fetchHasSubscription = async (user_url, query) => {
        try {
            const response = await fetchWithTokenRefresh(`${user_url}?${query}`, { method: 'GET' });
            const data = await response.json();
            return data;
        } catch (error) {
            console.error("Error during search:", error);
            return false;
        }
    };

    const updateFollowers = async (subscription_url, query, method) => {
        try {
            const response = await fetchWithTokenRefresh(`${subscription_url}?${query}`, { method: method });
            if (response.status === 200) {
                return true;
            }
        } catch (error) {
            console.error("Error during update:", error);
            return false;
        }
    };
    return { fetchUser, fetchFollowers, fetchSubscriptions, fetchHasSubscription };
}