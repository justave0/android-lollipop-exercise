package com.codepath.android.lollipopexercise.adapters;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.codepath.android.lollipopexercise.R;
import com.codepath.android.lollipopexercise.activities.ContactsActivity;
import com.codepath.android.lollipopexercise.activities.DetailsActivity;
import com.codepath.android.lollipopexercise.models.Contact;

import org.parceler.Parcels;

import java.util.List;

// Provide the underlying view for an individual list item.
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {
    private Activity mContext;
    private List<Contact> mContacts;

    public ContactsAdapter(Activity context, List<Contact> contacts) {
        mContext = context;
        if (contacts == null) {
            throw new IllegalArgumentException("contacts must not be null");
        }
        mContacts = contacts;


    }

    // Inflate the view based on the viewType provided.
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final Contact contact = mContacts.get(position);
        holder.rootView.setTag(contact);
        holder.tvName.setText(contact.getName());
        //Glide.with(mContext).load(contact.getThumbnailDrawable()).centerCrop().into(holder.ivProfile);
        // Use Glide to get a callback with a Bitmap which can then
        // be used to extract a vibrant color from the Palette.

        // Define an asynchronous listener for image loading
        final CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                // TODO 1. Instruct Glide to load the bitmap into the `holder.ivProfile` profile image view
                Glide.with(mContext)
                        .asBitmap()
                        .load(contact.getThumbnailDrawable()).centerCrop()
                        .into(holder.ivProfile);
                // TODO 2. Use generate() method from the Palette API to get the vibrant color from the bitmap
                Palette palette = Palette.from(resource).generate();
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                // Set the result as the background color for `holder.vPalette` view containing the contact's name.
                holder.vPalette.setBackgroundColor(vibrant.getRgb());
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                // can leave empty
            }
        };

// Instruct Glide to load the bitmap into the asynchronous target defined above
        Glide.with(mContext).asBitmap().load(contact.getThumbnailDrawable()).centerCrop().into(target);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    // Provide a reference to the views for each contact item
    public class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivProfile;
        final TextView tvName;
        final View vPalette;

        public VH(View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivProfile = (ImageView)itemView.findViewById(R.id.ivProfile);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            vPalette = itemView.findViewById(R.id.vPalette);

            // Navigate to contact details activity on click of card view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Contact contact = (Contact)v.getTag();
                    if (contact != null) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra(DetailsActivity.EXTRA_CONTACT, Parcels.wrap(contact));

                        Pair<View, String> p1 = Pair.create((View) ivProfile, "profile");
                        Pair<View, String> p2 = Pair.create(vPalette, "palette");
                        Pair<View, String> p3 = Pair.create((View)tvName, "text");
                        //ActivityOptionsCompat options = makeSceneTransitionAnimation(mContext, p1, p2, p3);
                        ActivityOptionsCompat options = makeSceneTransitionAnimation(mContext, p1, p2, p3);

                        mContext.startActivity(intent, options.toBundle());
                    }
                }
            });
        }
    }

}
